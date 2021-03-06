/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.map;

import com.hazelcast.nio.Address;
import com.hazelcast.nio.IOUtil;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class EventData implements DataSerializable {

    protected String source;
    protected String mapName;
    protected Address caller;
    protected Data dataKey;
    protected Data dataNewValue;
    protected Data dataOldValue;
    protected int eventType;

    public EventData() {
    }

    public EventData(String source, String mapName, Address caller, Data dataKey, Data dataNewValue, Data dataOldValue, int eventType) {
        this.source = source;
        this.mapName = mapName;
        this.caller = caller;
        this.dataKey = dataKey;
        this.dataNewValue = dataNewValue;
        this.dataOldValue = dataOldValue;
        this.eventType = eventType;
    }

    public String getSource() {
        return source;
    }

    public String getMapName() {
        return mapName;
    }

    public Address getCaller() {
        return caller;
    }

    public Data getDataKey() {
        return dataKey;
    }

    public Data getDataNewValue() {
        return dataNewValue;
    }

    public Data getDataOldValue() {
        return dataOldValue;
    }

    public int getEventType() {
        return eventType;
    }

    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(source);
        out.writeUTF(mapName);
        out.writeObject(caller);
        dataKey.writeData(out);
        IOUtil.writeNullableData(out, dataNewValue);
        IOUtil.writeNullableData(out, dataOldValue);
        out.writeInt(eventType);
    }

    public void readData(ObjectDataInput in) throws IOException {
        source = in.readUTF();
        mapName = in.readUTF();
        caller = in.readObject();
        dataKey = IOUtil.readData(in);
        dataNewValue = IOUtil.readNullableData(in);
        dataOldValue = IOUtil.readNullableData(in);
        eventType = in.readInt();
    }

    public Object cloneWithoutValues() {
        return new EventData(source, mapName, caller, dataKey, null, null, eventType);
    }
}
