/* ./src/main/java/bdglue2/publisher/kafka/KafaMessageTableKey.java 
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
package bdglue2.publisher.kafka;

import bdglue2.encoder.EventData;

import bdglue2.encoder.EventHeader;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Return the table name as the topic, and a representation of the
 * primary key as the Kafka message key.
 * 
 */
public class KafkaMessageTableKey implements KafkaMessageHelper {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaMessageTableKey.class);
    
    public KafkaMessageTableKey() {
        super();
        
        LOG.debug("KafkaMessageTableKey() has been loaded");
    }

    @Override
    public String getTopic(EventData evt) {
        return evt.getMetaValue(EventHeader.TABLE);
    }

    @Override
    public String getMessageKey(EventData evt) {
        return evt.getMetaValue(EventHeader.ROWKEY);
    }


    @Override
    public void setTopic(String topic) {
        // not implemented
    }

    @Override
    public void setMessageKey(String key) {
        // not implemented
    }
}
