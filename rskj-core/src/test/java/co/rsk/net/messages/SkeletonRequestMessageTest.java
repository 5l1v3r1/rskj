/*
 * This file is part of RskJ
 * Copyright (C) 2017 RSK Labs Ltd.
 * (derived from ethereumJ library, Copyright (c) 2016 <ether.camp>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package co.rsk.net.messages;

import co.rsk.net.MessageProcessingVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SkeletonRequestMessageTest {

    @Test
    public void createMessage() {
        long id = 42;
        long startNumber = 18;
        SkeletonRequestMessage message = new SkeletonRequestMessage(id, startNumber);

        assertEquals(MessageType.SKELETON_REQUEST_MESSAGE, message.getMessageType());
        assertEquals(id, message.getId());
        assertEquals(startNumber, message.getStartNumber());
    }

    @Test
    public void accept() {
        SkeletonRequestMessage message = new SkeletonRequestMessage(1, 10);

        MessageVisitor visitor = mock(MessageProcessingVisitor.class);

        message.accept(visitor);

        verify(visitor, times(1)).apply(message);
    }
}
