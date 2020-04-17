/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.plugin.sfmc.source.util;

import io.cdap.cdap.api.data.schema.Schema;

import java.util.List;

import static io.cdap.plugin.sfmc.source.util.SalesforceConstants.DATA_EXTENSION_PREFIX;

/**
 * Information about a Salesforce table.
 */
public class SalesforceObjectInfo {
  private final SourceObject object;
  private final String dataExtensionKey;
  private final Schema schema;
  private final int recordCount;

  /**
   * Constructor for SalesforceObjectInfo for non-dataextension object.
   * @param object      The Salesforce Marketing Cloud object
   * @param columns     The list of columns
   * @param recordCount The total number of records
   */
  public SalesforceObjectInfo(SourceObject object, List<SalesforceColumn> columns, int recordCount) {
    this(object, null, columns, recordCount);
  }

  /**
   * Constructor for SalesforceObjectInfo for dataextension object.
   *
   * @param object            The Salesforce Marketing Cloud object as DataExtension
   * @param dataExtensionKey  The data extension key
   * @param columns           The list of columns
   * @param recordCount       The total number of records
   */
  public SalesforceObjectInfo(SourceObject object, String dataExtensionKey, List<SalesforceColumn> columns,
                              int recordCount) {
    this.object = object;
    this.dataExtensionKey = dataExtensionKey;
    SchemaBuilder schemaBuilder = new SchemaBuilder();
    this.schema = schemaBuilder.constructSchema(getTableName(), columns);
    this.recordCount = recordCount;
  }

  public SourceObject getObject() {
    return object;
  }

  /**
   * Returns the table name for the object.
   *
   * @return  In case of Data Extension, it returns name in `dataextension_[data extension key]` format
   * Otherwise, it returns object name
   */
  public String getTableName() {
    if (getObject() == SourceObject.DATA_EXTENSION) {
      return String.format("%s%s", DATA_EXTENSION_PREFIX, dataExtensionKey);
    } else {
      return getObject().getTableName();
    }
  }

  public Schema getSchema() {
    return schema;
  }

  public int getRecordCount() {
    return recordCount;
  }
}
