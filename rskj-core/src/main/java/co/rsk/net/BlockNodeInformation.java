/*
 * This file is part of RskJ
 * Copyright (C) 2017 RSK Labs Ltd.
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

package co.rsk.net;

import co.rsk.crypto.Keccak256;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * BlockNodeInformation has information about which blocks are known by which peer,
 * and provides convenient functions to retrieve all the blocks known by a node, and
 * which nodes know a certain block.
 * <p>
 * BlockNodeInformation will only hold a limited amount of blocks and peers. Blocks
 * that aren't accessed frequently will be deleted, as well as peers.
 * Peers will only remember the last maxBlocks blocks that were inserted.
 */
public class BlockNodeInformation {
    private final LinkedHashMap<Keccak256, Set<NodeID>> nodesByBlock;
    private final int maxBlocks;

    public BlockNodeInformation(final int maxBlocks) {
        this.maxBlocks = maxBlocks;
        // Blocks are evicted in Least-recently-accessed order.
        nodesByBlock = new LinkedHashMap<Keccak256, Set<NodeID>>(BlockNodeInformation.this.maxBlocks, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Keccak256, Set<NodeID>> eldest) {
                return size() > BlockNodeInformation.this.maxBlocks;
            }
        };
    }

    public BlockNodeInformation() {
        this(1000);
    }

    /**
     * addBlockToNode specifies that a given node knows about a given block.
     *
     * @param blockHash the block hash.
     * @param nodeID    the node to add the block to.
     */
    public synchronized void addBlockToNode(@Nonnull final Keccak256 blockHash, @Nonnull final NodeID nodeID) {
        Set<NodeID> blockNodes = nodesByBlock.get(blockHash);
        if (blockNodes == null) {
            // Create a new set for the nodes that know about a block.
            // There is no peer eviction, because there are few peers compared to blocks.
            blockNodes = new HashSet<>();
            nodesByBlock.put(blockHash, blockNodes);
        }

        blockNodes.add(nodeID);
    }
    
    /**
     * getNodesByBlock retrieves all the nodes that contain a given block.
     *
     * @param blockHash the block's hash.
     * @return A set containing all the nodes that have that block.
     */
    @Nonnull
    public synchronized Set<NodeID> getNodesByBlock(@Nonnull final Keccak256 blockHash) {
        Set<NodeID> result = nodesByBlock.get(blockHash);
        if (result == null) {
            result = new HashSet<>();
        }
        return new HashSet<>(result);
    }

    /**
     * getNodesByBlock is a convenient function to avoid creating a Keccak256.
     *
     * @param blockHash the block hash.
     * @return all the nodeIDs that contain the given block.
     */
    @Nonnull
    public Set<NodeID> getNodesByBlock(@Nonnull final byte[] blockHash) {
        return getNodesByBlock(new Keccak256(blockHash));
    }

}
