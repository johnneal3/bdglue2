/* ./src/main/java/bdglue2/publisher/BDGluePublisher.java 
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
package bdglue2.publisher;

import bdglue2.encoder.EventData;

/**
 * An interface that must be implemented by all Publishers.
 */
public interface BDGluePublisher {
    /**
     * Connect to the target.
     */
    void connect();

    /**
     * Format the event and write it to the target.
     * 
     * @param threadName the name of the calling thread.
     * @param evt the encoded event.
     */
    void writeEvent(String threadName, EventData evt);
    
    /**
     * Close connections and clean up as needed.
     */
    void cleanup();
}
