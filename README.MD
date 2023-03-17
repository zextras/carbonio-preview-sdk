<!--
SPDX-FileCopyrightText: 2022 Zextras <https://www.zextras.com

SPDX-License-Identifier: AGPL-3.0-only
-->

<div align="center">
  <h1>Carbonio-Preview-Sdk 🚀 </h1>
</div>

<div align="center">

Official sdk for Preview backend service for Zextras Carbonio

[![Contributors][contributors-badge]][contributors]
[![Activity][activity-badge]][activity]
[![License][license-badge]](COPYING)
[![Project][project-badge]][project]
[![Twitter][twitter-badge]][twitter]

</div>

## Dependency installation 🏁

```xml

<dependency>
  <groupId>com.zextras.carbonio.preview</groupId>
  <artifactId>carbonio-preview-sdk</artifactId>
  <version>${preview-sdk.version}</version>
</dependency>
```

## Usage 📈

```java
import com.zextras.carbonio.preview.queries.BlobResponse;
import com.zextras.carbonio.preview.queries.Query;
import com.zextras.carbonio.preview.queries.enums.ServiceType;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class Test {

  public static void main(String[] argv) throws FileNotFoundException {
    String fileId = "3778a545-55e9-4ba8-a131-6abe1cb08d20";
    int version = 1;
    String accountId = "364bfa07-fafe-4f34-9a92-9fa515065c43";
    ServiceType type = ServiceType.FILES;

    // Data initialization
    Query imagePreviewQuery = new Query.QueryBuilder(accountId, fileId, version, type)
      .setCrop(true)
      .setPreviewArea("300x200")
      .build();

    PreviewClient previewClient = PreviewClient.atURL("http://127.0.0.1:10000");

    // GET
    previewClient
      .getPreviewOfImage(imagePreviewQuery)
      .onSuccess(BlobResponse::getContent);

    // POST
    FileInputStream blob = new FileInputStream("/tmp/image.jpeg");
    previewClient.postPreviewOfImage(blob, imagePreviewQuery, "filename")
      .onSuccess(BlobResponse::getContent);
  }
}
```

The build method closes the chain and returns a Query object used to call the PreviewClient. The
build method does not check for validity of the given input, a wrong query will be signaled only
once the PreviewClient sends the query.

## License
Official sdk for Preview backend service for Zextras Carbonio.

Released under the AGPL-3.0-only license as specified here: [COPYING](COPYING).

Copyright (C) 2022 Zextras <https://www.zextras.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

See [COPYING](COPYING) file for the project license details

See [THIRDPARTIES](THIRDPARTIES) file for other licenses details

### Copyright notice

All non-software material (such as, for example, names, images, logos, sounds) is owned by Zextras
s.r.l. and is licensed under [CC-BY-NC-SA](https://creativecommons.org/licenses/by-nc-sa/4.0/).

Where not specified, all source files owned by Zextras s.r.l. are licensed under AGPL-3.0-only


[contributors-badge]: https://img.shields.io/github/contributors/zextras/carbonio-preview-sdk "Contributors"

[contributors]: https://github.com/zextras/carbonio-preview-sdk/graphs/contributors "Contributors"

[activity-badge]: https://img.shields.io/github/commit-activity/m/zextras/carbonio-preview-sdk "Activity"

[activity]: https://github.com/zextras/carbonio-preview-sdk/pulse "Activity"

[license-badge]: https://img.shields.io/badge/license-AGPL-blue.svg

[project-badge]: https://img.shields.io/badge/project-carbonio-informational "Project Carbonio"

[project]: https://www.zextras.com/carbonio/ "Project Carbonio"

[twitter-badge]: https://img.shields.io/twitter/follow/zextras?style=social&logo=twitter "Follow on Twitter"

[twitter]: https://twitter.com/intent/follow?screen_name=zextras "Follow Zextras on Twitter"
