/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.android.exoplayer2.testutil.TestUtil;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import okhttp3.internal.Util;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

/** {@link DataSource} contract tests for {@link FileDataSource}. */
@RunWith(AndroidJUnit4.class)
public class FileDataSourceContractTest extends DataSourceContractTest {

  private static final byte[] DATA = TestUtil.buildTestData(20);

  @Rule public final TemporaryFolder tempFolder = new TemporaryFolder();

  private Uri simpleUri;
  private Uri zeroBytesUri;

  @Before
  public void writeFiles() throws Exception {
    simpleUri = writeFile(DATA);
    zeroBytesUri = writeFile(Util.EMPTY_BYTE_ARRAY);
  }

  @Override
  protected ImmutableList<TestResource> getTestResources() {
    return ImmutableList.of(
        new TestResource.Builder()
            .setName("simple")
            .setUri(simpleUri)
            .setExpectedBytes(DATA)
            .build(),
        new TestResource.Builder()
            .setName("zero-bytes")
            .setUri(zeroBytesUri)
            .setExpectedBytes(Util.EMPTY_BYTE_ARRAY)
            .build());
  }

  @Override
  protected Uri getNotFoundUri() {
    return Uri.fromFile(tempFolder.getRoot().toPath().resolve("nonexistent").toFile());
  }

  @Override
  protected DataSource createDataSource() {
    return new FileDataSource();
  }

  private Uri writeFile(byte[] data) throws IOException {
    File file = tempFolder.newFile();
    Files.write(Paths.get(file.getAbsolutePath()), data);
    return Uri.fromFile(file);
  }
}
