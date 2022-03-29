// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: AGPL-3.0-only

package com.zextras.carbonio.preview.queries;

import com.zextras.carbonio.preview.queries.enums.Format;
import com.zextras.carbonio.preview.queries.enums.Quality;
import com.zextras.carbonio.preview.queries.enums.ServiceType;
import com.zextras.carbonio.preview.queries.enums.Shape;

import java.util.Optional;

public class Query {

  // Required if using non ce version
  private final String      nodeOwnerId;
  //Required if using a get
  private final String      nodeId;
  private final Integer     version;
  private final ServiceType type;
  //Image required always
  private final String      previewArea;
  //Image optional
  private final Shape       shape;
  private final Quality     quality;
  private final Format      outputFormat;
  private final String      crop;
  //Pdf optional
  private final Integer     firstPage;
  private final Integer     lastPage;


  private Query(QueryBuilder builder) {
    this.nodeOwnerId = builder.nodeOwnerId;
    this.nodeId = builder.nodeId;
    this.version = builder.version;
    this.type = builder.type;
    this.previewArea = builder.previewArea;
    this.shape = builder.shape;
    this.quality = builder.quality;
    this.outputFormat = builder.outputFormat;
    this.crop = builder.crop;
    this.firstPage = builder.firstPage;
    this.lastPage = builder.lastPage;
  }

  public Optional<String> getNodeOwnerId() {
    return Optional.ofNullable(nodeOwnerId);
  }

  public Optional<String> getNodeId() {
    return Optional.ofNullable(nodeId);
  }

  public Optional<Integer> getVersion() {
    return Optional.ofNullable(version);
  }

  public Optional<String> getType() {
    return Optional.ofNullable(
      type == null
        ? null
        : type.toString().toLowerCase()
    );
  }

  public Optional<String> getPreviewArea() {
    return Optional.ofNullable(previewArea);
  }

  public Optional<String> getShape() {
    return Optional.ofNullable(
      shape == null
        ? null
        : shape.toString().toLowerCase()
    );
  }

  public Optional<String> getQuality() {
    return Optional.ofNullable(
      quality == null
        ? null
        : quality.toString().toLowerCase()
    );
  }

  public Optional<String> getOutputFormat() {
    return Optional.ofNullable(
      outputFormat == null
        ? null
        : outputFormat.toString().toLowerCase()
    );
  }

  public Optional<String> getCrop() {
    return Optional.ofNullable(crop);
  }

  public Optional<Integer> getLastPage() {
    return Optional.ofNullable(lastPage);
  }

  public Optional<Integer> getFirstPage() {
    return Optional.ofNullable(firstPage);
  }

  @Override
  public String toString() {
    StringBuilder baseUriBuilder = new StringBuilder();

    // Required parameters setup

    baseUriBuilder.append('/');
    getNodeId().ifPresent(n -> baseUriBuilder.append(n).append('/'));
    getVersion().ifPresent(v -> baseUriBuilder.append(v).append('/'));
    getPreviewArea().ifPresent(area -> baseUriBuilder.append(area).append('/'));

    // Optional parameters setup

    String baseUri = baseUriBuilder.toString();
    StringBuilder queryParameter = new StringBuilder();
    getShape().ifPresent(s -> queryParameter.append("shape=").append(s).append("&"));
    getQuality().ifPresent(q -> queryParameter.append("quality=").append(q).append("&"));
    getOutputFormat().ifPresent(f -> queryParameter.append("output_format=").append(f).append("&"));
    getCrop().ifPresent(c -> queryParameter.append("crop=").append(c).append("&"));
    getFirstPage().ifPresent(f -> queryParameter.append("first_page=").append(f).append("&"));
    getLastPage().ifPresent(l -> queryParameter.append("last_page=").append(l).append("&"));

    getType().ifPresent(t -> queryParameter.append("service_type=").append(t));
    int queryLength = queryParameter.length();
    if (queryLength > 0 && queryParameter.charAt(queryLength - 1) == '&') {
      queryParameter.deleteCharAt(queryLength - 1);
    }
    return
      (queryParameter.length() <= 0)
        ? baseUri
        : baseUri + '?' + queryParameter;
  }


  public static class QueryBuilder {

    //Required if using a get
    private String      nodeOwnerId;
    private String      nodeId;
    private Integer     version;
    private ServiceType type;
    //Image required always
    private String      previewArea;
    //Image optional
    private Shape       shape;
    private Quality     quality;
    private Format      outputFormat;
    private String      crop;
    //pdf optional
    private Integer     firstPage;
    private Integer     lastPage;

    public QueryBuilder(
      String nodeOwnerId,
      String nodeId,
      int version,
      ServiceType type
    ) {
      this.nodeOwnerId = nodeOwnerId;
      this.nodeId = nodeId;
      this.version = version;
      this.type = type;
    }

    public QueryBuilder(
      String nodeId,
      int version,
      ServiceType type
    ) {
      this.nodeId = nodeId;
      this.version = version;
      this.type = type;
    }

    public QueryBuilder(ServiceType type) {
      this.type = type;
    }

    public QueryBuilder() {

    }

    public QueryBuilder setServiceType(ServiceType type) {
      this.type = type;
      return this;
    }

    public QueryBuilder setServiceType(String type) {
      this.type = ServiceType.valueOf(type);
      return this;
    }

    public QueryBuilder setNodeOwnerId(String nodeOwnerId) {
      this.nodeOwnerId = nodeOwnerId;
      return this;
    }

    public QueryBuilder setNodeId(String nodeId) {
      this.nodeId = nodeId;
      return this;
    }

    public QueryBuilder setVersion(int version) {
      this.version = version;
      return this;
    }

    public QueryBuilder setPreviewArea(String previewArea) {
      this.previewArea = previewArea;
      return this;
    }

    public QueryBuilder setShape(Shape shape) {
      this.shape = shape;
      return this;
    }

    public QueryBuilder setShape(String shape) {
      this.shape = Shape.valueOf(shape);
      return this;
    }

    public QueryBuilder setQuality(Quality quality) {
      this.quality = quality;
      return this;
    }

    public QueryBuilder setQuality(String quality) {
      this.quality = Quality.valueOf(quality);
      return this;
    }

    public QueryBuilder setOutputFormat(Format outputFormat) {
      this.outputFormat = outputFormat;
      return this;
    }

    public QueryBuilder setOutputFormat(String outputFormat) {
      this.outputFormat = Format.valueOf(outputFormat);
      return this;
    }

    public QueryBuilder setCrop(boolean crop) {
      this.crop = crop
        ? "true"
        : "false";
      return this;
    }

    public QueryBuilder setFirstPage(int firstPage) {
      this.firstPage = firstPage;
      return this;
    }

    public QueryBuilder setLastPage(int lastPage) {
      this.lastPage = lastPage;
      return this;
    }

    public Query build() {
      return new Query(this);
    }
  }

}
