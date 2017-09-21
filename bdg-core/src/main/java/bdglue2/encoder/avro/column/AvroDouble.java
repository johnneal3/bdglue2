/* ./src/main/java/bdglue2/encoder/avro/column/AvroDouble.java 
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
 * Methods supporting Double length floating point data.
 *
 */
public class AvroDouble extends AvroColumn {
    /**
     * @param meta the meta data for this column.
     */
    public AvroDouble(DownstreamColumnMetaData meta) {
        super(meta);
        setColumnType();
    }

    /**
     * Set the default values for this column type.
     */
    @Override
    protected void setColumnType() {
        super.columnType = AvroColumnType.DOUBLE;
        super.avroSchemaType = Schema.Type.DOUBLE;
    }

    /**
     * Encode the data for this column.
     * 
     * @param col The column data we want to encode
     * @return An instance of Double that will be added 
     * to the Avro-encoded data stream.
     */
    @Override
    public Double encodeForAvro(DownstreamColumnData col) {
        return new Double(col.asDouble());
    }
}
