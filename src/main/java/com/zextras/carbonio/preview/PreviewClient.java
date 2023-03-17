// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: AGPL-3.0-only

package com.zextras.carbonio.preview;

import com.zextras.carbonio.preview.exceptions.BadRequest;
import com.zextras.carbonio.preview.exceptions.InternalServerError;
import com.zextras.carbonio.preview.exceptions.ItemNotFound;
import com.zextras.carbonio.preview.exceptions.ValidationError;
import com.zextras.carbonio.preview.queries.BlobResponse;
import com.zextras.carbonio.preview.queries.Query;
import io.vavr.control.Try;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * An HTTP client that allows to execute HTTP requests to the Preview service. This is the main
 * class needed to call the Preview APIs.
 */
public class PreviewClient {

  private final String previewEndpoint;
  private final String previewUrl;
  private final String imageEndpoint       = "image";
  private final String pdfEndpoint         = "pdf";
  private final String documentEndpoint    = "document";
  private final String healthReadyEndpoint = "/health/ready/";
  private final String healthLiveEndpoint = "/health/live/";
  private final String thumbnailPathParam = "thumbnail";
  private final String fileOwnerIdHeader  = "FileOwnerId";

  // UTILITY

  PreviewClient(String previewURL) {
    this.previewUrl = previewURL;
    this.previewEndpoint = previewURL + "/preview";
  }


  /**
   * Creates a new instance of the {@link PreviewClient}.
   *
   * @param url is a {@link String} representing the url used to communicate with the Preview
   * service. The expected url form must be as follows:
   * <code>protocol://ip:port</code> (for example <code>http://127.0.0.1:8080</code>).
   *
   * @return an instance of the {@link PreviewClient}.
   */
  public static PreviewClient atURL(String url) {
    return new PreviewClient(url);
  }

  /**
   * Creates a new instance of the {@link PreviewClient}.
   *
   * @param protocol is a {@link String} representing the protocol used to communicate with the
   * Preview service (for example: <code>http</code>).
   * @param domain is a {@link String} representing the domain used to communicate with the Preview
   * service (for example: <code>127.0.0.1</code>).
   * @param port is an {@link Integer} representing the port used to communicate with the Preview
   * service.
   *
   * @return an instance of the {@link PreviewClient}.
   */
  public static PreviewClient atURL(
    String protocol,
    String domain,
    Integer port
  ) {
    return new PreviewClient(protocol + "://" + domain + ":" + port);
  }

  private String createPathForThumbnail(Query query) {
    String toModifyRequestUri = query.toString();
    // 1 because an empty query contains '/'
    if (toModifyRequestUri.chars().filter(ch -> ch == '/').count() > 1) {
      int index = toModifyRequestUri.indexOf('?');
      if (index == -1) {
        return toModifyRequestUri + "/" + thumbnailPathParam + "/";
      } else {
        return toModifyRequestUri.substring(0, index - 1) + "/" + thumbnailPathParam
          + toModifyRequestUri.substring(index - 1);
      }
    } else {
      return "/" + thumbnailPathParam + "/";
    }
  }

  // IMAGE

  /**
   * Allows to request the processing of an IMAGE to the PREVIEW endpoint using an HTTP GET
   *
   * @param query is a {@link Query} that specifies the query parameters for the GET.
   *
   * @return a {@link Try} of {@link BlobResponse} representing the image content if everything went
   * ok.
   */
  public Try<BlobResponse> getPreviewOfImage(Query query) {
    return sendGetToPreviewService(query.toString(), imageEndpoint, query.getFileOwnerId().get());
  }


  /**
   * Allows to request the processing of an IMAGE to the THUMBNAIL endpoint using an HTTP GET
   *
   * @param query is a {@link Query} that specifies the query parameters for the GET.
   *
   * @return a {@link Try} of {@link BlobResponse} representing the image content if everything went
   * ok.
   */
  public Try<BlobResponse> getThumbnailOfImage(Query query) {
    return sendGetToPreviewService(
      createPathForThumbnail(query), imageEndpoint, query.getFileOwnerId().get()
    );
  }


  /**
   * Allows to send an IMAGE to the PREVIEW endpoint using an HTTP POST and be processed
   *
   * @param blob is a {@link InputStream} that contains the image.
   * @param query is a {@link Query} that specifies the query parameters
   * @param fileName is a {@link String} representing the name of the file.
   *
   * @return a {@link Try} of {@link BlobResponse} representing the image content if everything went
   * ok.
   */
  public Try<BlobResponse> postPreviewOfImage(
    InputStream blob,
    Query query,
    String fileName
  ) {
    return sendPostToPreviewService(blob, fileName, query.toString(), imageEndpoint);
  }

  /**
   * Allows to send an IMAGE to the THUMBNAIL endpoint using an HTTP POST and be processed
   *
   * @param blob is a {@link InputStream} that contains the image.
   * @param query is a {@link Query} that specifies the query parameters
   * @param fileName is a {@link String} representing the name of the file.
   *
   * @return a {@link Try} of {@link BlobResponse} representing the image content if everything went
   * ok.
   */
  public Try<BlobResponse> postThumbnailOfImage(
    InputStream blob,
    Query query,
    String fileName
  ) {
    return sendPostToPreviewService(blob, fileName, createPathForThumbnail(query), imageEndpoint);
  }

  //PDF

  /**
   * Allows to request the processing of a PDF to the PREVIEW endpoint using an HTTP GET
   *
   * @param query is a {@link Query} that specifies the query parameters for the GET.
   *
   * @return a {@link Try} of {@link BlobResponse} representing the pdf content if everything went
   * ok.
   */
  public Try<BlobResponse> getPreviewOfPdf(Query query) {
    return sendGetToPreviewService(query.toString(), pdfEndpoint, query.getFileOwnerId().get());
  }


  /**
   * Allows to request the processing of a PDF to the THUMBNAIL endpoint using an HTTP GET
   *
   * @param query is a {@link Query} that specifies the query parameters for the GET.
   *
   * @return a {@link Try} of {@link BlobResponse} representing the image content if everything went
   * ok.
   */
  public Try<BlobResponse> getThumbnailOfPdf(Query query) {
    return sendGetToPreviewService(
      createPathForThumbnail(query), pdfEndpoint, query.getFileOwnerId().get()
    );
  }

  /**
   * Allows to send a PDF to the THUMBNAIL endpoint using an HTTP POST and be processed
   *
   * @param blob is a {@link InputStream} that contains the pdf.
   * @param query is a {@link Query} that specifies the query parameters
   * @param fileName is a {@link String} representing the name of the file.
   *
   * @return a {@link Try} of {@link BlobResponse} representing the image content if everything went
   * ok.
   */
  public Try<BlobResponse> postThumbnailOfPdf(
    InputStream blob,
    Query query,
    String fileName
  ) {
    return sendPostToPreviewService(blob, fileName, createPathForThumbnail(query), pdfEndpoint);
  }

  /**
   * Allows to send a PDF to the PREVIEW endpoint using an HTTP POST and be processed
   *
   * @param blob is a {@link InputStream} that contains the pdf.
   * @param query is a {@link Query} that specifies the query parameters
   * @param fileName is a {@link String} representing the name of the file.
   *
   * @return a {@link Try} of {@link BlobResponse} representing the pdf content if everything went
   * ok.
   */
  public Try<BlobResponse> postPreviewOfPdf(
    InputStream blob,
    Query query,
    String fileName
  ) {
    return sendPostToPreviewService(blob, fileName, query.toString(), pdfEndpoint);
  }

  //DOCUMENT

  /**
   * Allows to request the processing of a DOCUMENT to the PREVIEW endpoint using an HTTP GET
   *
   * @param query is a {@link Query} that specifies the query parameters for the GET.
   *
   * @return a {@link Try} of {@link BlobResponse} representing the pdf content if everything went
   * ok.
   */
  public Try<BlobResponse> getPreviewOfDocument(Query query) {
    return sendGetToPreviewService(
      query.toString(), documentEndpoint, query.getFileOwnerId().get()
    );
  }

  /**
   * Allows to request the processing of a DOCUMENT to the THUMBNAIL endpoint using an HTTP GET
   *
   * @param query is a {@link Query} that specifies the query parameters for the GET.
   *
   * @return a {@link Try} of {@link BlobResponse} representing the image content if everything went
   * ok.
   */
  public Try<BlobResponse> getThumbnailOfDocument(Query query) {
    return sendGetToPreviewService(
      createPathForThumbnail(query), documentEndpoint, query.getFileOwnerId().get()
    );
  }


  /**
   * Allows to send a DOCUMENT to the THUMBNAIL endpoint using an HTTP POST and be processed
   *
   * @param blob is a {@link InputStream} that contains the document.
   * @param query is a {@link Query} that specifies the query parameters
   * @param fileName is a {@link String} representing the name of the file.
   *
   * @return a {@link Try} of {@link BlobResponse} representing the image content if everything went
   * ok.
   */
  public Try<BlobResponse> postThumbnailOfDocument(
    InputStream blob,
    Query query,
    String fileName
  ) {
    return sendPostToPreviewService(
      blob, fileName, createPathForThumbnail(query), documentEndpoint
    );
  }

  /**
   * Allows to send a DOCUMENT to the PREVIEW endpoint using an HTTP POST and be processed
   *
   * @param blob is a {@link InputStream} that contains the document.
   * @param query is a {@link Query} that specifies the query parameters
   * @param fileName is a {@link String} representing the name of the file.
   *
   * @return a {@link Try} of {@link BlobResponse} representing the pdf content if everything went
   * ok.
   */
  public Try<BlobResponse> postPreviewOfDocument(
    InputStream blob,
    Query query,
    String fileName
  ) {
    return sendPostToPreviewService(blob, fileName, query.toString(), documentEndpoint);
  }

  // API CALL

  private Try<BlobResponse> sendPostToPreviewService(
    InputStream blob,
    String fileName,
    String query,
    String endpoint
  ) {
    String requestUri = MessageFormat.format(
      "{0}/{1}{2}",
      previewEndpoint, endpoint, query
    );
    HttpPost httpPost = new HttpPost(requestUri);

    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.addBinaryBody("file", blob, ContentType.APPLICATION_OCTET_STREAM, fileName);
    HttpEntity multipart = builder.build();
    httpPost.setEntity(multipart);
    return sendRequestToPreviewService(httpPost);

  }

  private Try<BlobResponse> sendGetToPreviewService(
    String query,
    String endpoint,
    String accountHeaderValue
  ) {
    String requestUri = MessageFormat.format(
      "{0}/{1}{2}",
      previewEndpoint, endpoint, query
    );
    HttpGet request = new HttpGet(requestUri);
    request.setHeader(fileOwnerIdHeader, accountHeaderValue);
    return sendRequestToPreviewService(request);
  }

  private Try<BlobResponse> sendRequestToPreviewService(HttpRequestBase request) {

    try {
      // The response is not consumed in this code block so if the http client is closed then also
      // the communication with the service will be closed breaking the response stream of the blob
      CloseableHttpClient httpClient = HttpClients.createMinimal();
      CloseableHttpResponse response = httpClient.execute(request);
      int statusCode = response.getStatusLine().getStatusCode();
      switch (statusCode) {
        case HttpStatus.SC_OK:
          return Try.success(new BlobResponse(response.getEntity()));
        case HttpStatus.SC_NOT_FOUND:
          return Try.failure(new ItemNotFound());
        case HttpStatus.SC_UNPROCESSABLE_ENTITY:
          return Try.failure(new ValidationError());
        case HttpStatus.SC_BAD_REQUEST:
          return Try.failure(new BadRequest());
        default:
          return Try.failure(new InternalServerError());
      }
    } catch (IOException exception) {
      return Try.failure(new InternalServerError(exception));
    }
  }

  public boolean healthReady() {
    return checkHealthStatus(healthReadyEndpoint);
  }

  public boolean healthLive() {
    return checkHealthStatus(healthLiveEndpoint);
  }

  private boolean checkHealthStatus(String endpoint) {
    try (CloseableHttpClient httpClient = HttpClients.createMinimal()) {

      String requestUri = MessageFormat.format(
        "{0}{1}",
        previewUrl, endpoint
      );
      HttpGet request = new HttpGet(requestUri);

      try (CloseableHttpResponse response = httpClient.execute(request)) {
        return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
      } catch (IOException exception) {
        return false;
      }
    } catch (IOException exception) {
      return false;
    }
  }
}
