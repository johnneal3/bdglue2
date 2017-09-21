/* ./src/main/java/bdglue2/encoder/avro/column/AvroLong.java 
 *
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bdglue2.encoder.avro.column;


import bdglue2.meta.schema.DownstreamColumnMetaData;
import bdglue2.meta.transaction.DownstreamColumnData;

import org.apache.avro.Schema;

/**
 * Methods supporting Long integer data.
 *
 */
public class AvroLong extends AvroColumn {
    /**
     * @param meta The meta data for this column.
     */
    public AvroLong(DownstreamColumnMetaData meta) {
        super(meta);
    }

    /**
     * Set the default values for this column type.
     */
    @Override
    protected void setColumnType() {
        super.columnType = AvroColumnType.LONG;
        super.avroSchemaType = Schema.Type.LONG;
    }
    /**
     * Encode the data for this column.
     * 
     * @param col The column data we want to encode
     * @return an instance of Long that will be added to 
     * the Avro-encoded data stream.
     */
    @Override
    public Long encodeForAvro(DownstreamColumnData col) {
        return new Long(col.asLong());
    }
}
