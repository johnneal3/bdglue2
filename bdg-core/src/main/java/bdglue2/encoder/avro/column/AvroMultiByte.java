/* ./src/main/java/bdglue2/encoder/avro/column/AvroMultiByte.java 
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
import org.apache.avro.util.Utf8;

/**
 * Methods supporting multi-byte string data (i.e. unicode).
 *
 */
public class AvroMultiByte extends AvroColumn {
    /**
     * @param meta The meta data for this column.
     */
    public AvroMultiByte(DownstreamColumnMetaData meta) {
        super(meta);
        setColumnType();
    }

    /**
     * Set the default values for this column type. 
     */
    @Override
    protected void setColumnType() {
        super.columnType = AvroColumnType.MULTIBYTE;
        super.avroSchemaType = Schema.Type.STRING;
        super.columnTypeString = "multi-byte";
    }
    /**
     * Encode the data for this column. The assumption
     * currently is that the data is already in a Utf8-friendly
     * format allowing proper construction.
     * 
     * @param col The column data we want to encode
     * @return an instance of org.apache.avro.util.Utf8 that will 
     * be added to the Avro-encoded data stream. 
     */
    @Override
    public Utf8 encodeForAvro(DownstreamColumnData col) {
        return new Utf8(col.asString());
    }
}
