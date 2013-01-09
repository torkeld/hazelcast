/*
 * Copyright (c) 2008-2012, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.queue;

import com.hazelcast.core.ItemEventType;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.Notifier;
import com.hazelcast.spi.Operation;

import java.io.IOException;

/**
 * @ali 12/6/12
 */
public class RemoveOperation extends QueueBackupAwareOperation implements Notifier {

    private Data data;

    transient long itemId = -1;

    public RemoveOperation() {
    }

    public RemoveOperation(String name, Data data) {
        super(name);
        this.data = data;
    }

    public void run() throws Exception {
        QueueContainer container = getContainer();
        itemId = container.remove(data);
        response = itemId != -1;
    }

    public void afterRun() throws Exception {
        if (itemId != -1) {
            publishEvent(ItemEventType.REMOVED, data);
        }
    }

    public boolean shouldBackup() {
        return Boolean.TRUE.equals(response);
    }

    public Operation getBackupOperation() {
        return new RemoveBackupOperation(name, itemId);
    }

    public void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        out.writeData(data);
    }

    public void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        data = in.readData();
    }

    public boolean shouldNotify() {
        return itemId != -1;
    }

    public Object getNotifiedKey() {
        return name + ":offer";
    }
}