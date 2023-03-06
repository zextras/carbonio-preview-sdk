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
import com.zextras.carbonio.preview.queries.Query;
import com.zextras.carbonio.preview.queries.enums.ServiceType;

import java.io.FileInputStream;
import java.io.InputStream;

class Test {

  public static void main(String[] argv) {
    fileId = "fe81dea6-dc90-4468-8aac-e0ec71dbb49d";
    version = 1;
    accountId = "fe81dea6";
    type = ServiceType.FILES;
    // data initialization
    // GET
    Query imagePreviewQuery = new Query.QueryBuilder(accountId, fileId, version, type)
      .setCrop(True)
      .setPreviewArea("300x200")
      .build();
    PreviewClient prewClient = PreviewClient.atURL("http://127.0.0.1:25566");
    Try<BlobResponse> result = prewClient.getPreviewOfImage(imagePreviewQuery);
    // POST
    Query imagePreviewQuery = new Query.QueryBuilder()
      .setCrop(True)
      .setPreviewArea("300x200")
      .build();
    FileInputStream blob = new File("path");
    Try<BlobResponse> result = prewClient.postPreviewOfImage(blob, imagePreviewQuery, "filename");
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
