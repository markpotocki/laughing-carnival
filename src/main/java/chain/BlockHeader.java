package chain;

import java.io.Serializable;
import java.util.Date;

public class BlockHeader implements Serializable {

    private Short version;

    private int hashPrevBlock;

    private int hashMerkleRoot;

    private Date timestamp;

    private int targetInCompactForm;

    private Integer nonce;

    public BlockHeader(Short version, int hashPrevBlock, int hashMerkleRoot, Date timestamp, int targetInCompactForm, Integer nonce) {
        this.version = version;
        this.hashPrevBlock = hashPrevBlock;
        this.hashMerkleRoot = hashMerkleRoot;
        this.timestamp = timestamp;
        this.targetInCompactForm = targetInCompactForm;
        this.nonce = nonce;
    }

    public int getTarget() {
        return targetInCompactForm;
    }
}
