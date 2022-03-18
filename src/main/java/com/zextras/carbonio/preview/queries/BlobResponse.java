// SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com>
//
// SPDX-License-Identifier: AGPL-3.0-only

package com.zextras.carbonio.preview.queries;

import org.apache.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;

public class BlobResponse {

  private final InputStream content;
  private final long length;
  private final String mimeType;

  public BlobResponse(HttpEntity entity) throws IOException {
    this(
            entity.getContent(),
            entity.getContentLength(),
            entity.getContentType().getValue()
    );
  }

  private BlobResponse(InputStream content, long length, String mimeType) {
    this.content = content;
    this.length = length;
    this.mimeType = mimeType;
  }

  public InputStream getContent() {
    return content;
  }

  public long getLength() {
    return length;
  }

  public String getMimeType() {
    return mimeType;
  }
}
