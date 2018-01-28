package chain;

import java.io.Serializable;
import java.util.List;

public class Block implements Serializable {

    private List<Transaction> transactionsInBlock;

    private BlockHeader blockHeader;

    private Short transactionCount;

    public Block(List<Transaction> transactionsInBlock, BlockHeader blockHeader, Short transactionCount) {
        this.transactionsInBlock = transactionsInBlock;
        this.blockHeader = blockHeader;
        this.transactionCount = transactionCount;
    }

    public List<Transaction> getTransactionsInBlock() {
        return transactionsInBlock;
    }

    public BlockHeader getBlockHeader() {
        return blockHeader;
    }

    public Short getTransactionCount() {
        return transactionCount;
    }
}
