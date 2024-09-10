package io.collective.basic;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class Blockchain {

    private final LinkedList<Block> chain = new LinkedList<>();

    public boolean isEmpty() {
        return chain.isEmpty();
    }

    public void add(Block block) {
        chain.add(block);
    }

    public int size() {
        return chain.size();
    }

    public boolean isValid() throws NoSuchAlgorithmException {
        if (chain.isEmpty()) {
            return true;
        }

        var previousHash = chain.getFirst().getPreviousHash();

        if (!previousHash.equals(Block.GENESIS_PREVIOUS_HASH)) {
            return false;
        }

        for (var block : chain) {
            if (!isMined(block) || !block.getHash().equals(block.calculatedHash())
                    || !block.getPreviousHash().equals(previousHash)) {
                return false;
            }

            previousHash = block.getHash();
        }

        return true;
    }

    /// Supporting functions that you'll need.

    public static Block mine(Block block) throws NoSuchAlgorithmException {
        Block mined = new Block(block.getPreviousHash(), block.getTimestamp(), block.getNonce());

        while (!isMined(mined)) {
            mined = new Block(mined.getPreviousHash(), mined.getTimestamp(), mined.getNonce() + 1);
        }
        return mined;
    }

    public static boolean isMined(Block minedBlock) {
        return minedBlock.getHash().startsWith("00");
    }
}