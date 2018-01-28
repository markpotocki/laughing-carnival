package client;

import chain.Block;
import chain.Transaction;
import client.scripts.ScriptType;

import java.util.List;

public class Scripts {

    // OP_TXFS  Transaction Full Signiture: Requires signiture by both parties x01
    // OP_INFX  Influx Transaction: Used to test by influxing spendable x02
    // OP_RETU  Return Transaction: Used to return change from over charging x03

    public void transaction(ScriptType s, Transaction t, List<Block> blockchain) {

        switch(s) {
            case OP_TXFS:
                break;
            case OP_INFX:
                break;
            case OP_RETU:
                break;
        }
    }

    private boolean verifyTwoSidedTransaction(Transaction t, List<Block> blockchain) {
        return false;
    }



}
