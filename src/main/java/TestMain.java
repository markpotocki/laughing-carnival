import chain.Block;
import chain.BlockHeader;
import chain.Transaction;
import client.network.discovery.DiscoveryBroadcastThread;
import client.network.discovery.DiscoveryRecieveThread;
import client.scripts.ScriptType;
import client.verify.KeyGenerator;
import client.verify.SignitureVerification;
import org.apache.commons.lang3.SerializationUtils;
import structures.MerkelTree;

import java.security.KeyPair;
import java.util.*;
import java.util.stream.Collectors;

public class TestMain {

    private static LinkedList<Block> blockChain = new LinkedList<>();

    private static KeyPair master;

    private static int lineCounter = 1;

    private static List<Long> results = new LinkedList<>();

    private static final int n = 1000;

    public static void main(String...args) throws InterruptedException {
        firstBlock();
        //testFirstMerkel();
        //testTransactionSerialization();
        //statsTest();
        keyPairTest();
        //generateTestTransactions();
        startThreads();

    }

    private static void startThreads() {
        DiscoveryBroadcastThread broadcast = new DiscoveryBroadcastThread(9010);
        broadcast.start();

        DiscoveryRecieveThread receiver = new DiscoveryRecieveThread(9010, 5);
        receiver.start();
    }

    private static void makeTransactionWithSigniture() {
        KeyPair keys = KeyGenerator.generateKeys();
    }

    private static void firstBlock() {
        Transaction one = new Transaction(new Date(), "00", "00", ScriptType.OP_INVD, 0);
        blockChain.add(oneBlock(Collections.singletonList(one)));
    }

    private static Block oneBlock(List<Transaction> t) {
        boolean solved = false;
        int nonce = 1;

        while (true) {
            BlockHeader bhOne = new BlockHeader((short) 1, 0, 0, new Date(), 50000, nonce);
            Block b = new Block(t, bhOne, (short) 10);
            if(b.hashCode() < bhOne.getTarget())
                return b;
            else
                nonce++;
        }
    }/*

    // 11 ff 11 ff 11 ff 11 ff 11 ff - Mark
    // dd ee dd ee dd ee dd ee dd ee - Dad
    // cc 99 cc 99 cc 99 cc 99 cc 99 - Kevin
    private static void generateTestTransactions() {
        String cityBank = "00 00 00 00 00 00 00 00 00 00";
        String markAddress = "11 ff 11 ff 11 ff 11 ff 11 ff";
        String dadAddress = "dd ee dd ee dd ee dd ee dd ee";
        String kevinAddress = "cc 99 cc 99 cc 99 cc 99 cc 99";
        List<Transaction> trans = new LinkedList<>();
        master = KeyGenerator.generateKeys();
        // give mark 20 coins
        for (int i = 0; i < 49; i++)
            trans.add(new Transaction(new Date(), cityBank, markAddress, ScriptType.OP_INFX, 20));
        Transaction dd = new Transaction(new Date(), cityBank, ))
        generateBlocks(trans);
    }*/

    private static void generateBlocks(List<Transaction> transactions) {
        System.out.println("Generating blocks for test blockchain.");
        for (int i = 0; i < 50; i += 10) {
            TestMain.blockChain.add(createBlock(new ArrayList<Transaction>(transactions.subList(i, i+10))));
            System.out.println("Iterations: " + i + "\t\tMerkel root: " + getBlockMerkelRoot(blockChain));
        }
        System.out.println("Blocks have generated successfully");

        TestMain.blockChain.stream().collect(Collectors.toList());
    }

    private static Block createBlock(List<Transaction> transactions) {
        Block success = null;
        int nonce = 1;

        while(success == null) {
            success = getBlock(nonce, transactions);
        }
        return success;
    }

    private static Block getBlock(int nonce, List<Transaction> transactions) {
        BlockHeader header = new BlockHeader((short) 1, TestMain.blockChain.peekLast().hashCode(), getMerkelRoot(transactions), new Date(), 50000, nonce);
        Block b = new Block(transactions, header, (short) 30);

        if (b.hashCode() < header.getTarget())
            return b;
        else
            return null;
    }



    private static void keyPairTest() {
        final Date ts = new Date();
        final String sender = "26 aa 1b eb 9a e2 7e ae b2 6e";
        final String receiver = "59 fa fd 9a 36 74 21 0a 14 fe";

        Transaction transaction = new Transaction(ts, sender, receiver, ScriptType.OP_INVD, 50);

        KeyPair keyPair = KeyGenerator.generateKeys();

        byte[] signiture = SignitureVerification.generateSignature(keyPair.getPrivate(), transaction);

        System.out.println("Signiture generated for transaction: " + signiture);

        boolean ver = SignitureVerification.checkSigniture(keyPair.getPublic(),transaction, signiture);

        System.out.println("Signiture verifified: " + ver);
    }

    private static void statsTest() {
        System.out.println("Beginning stats test");
        for(int i = 0; i < n; i++) {
            System.out.println("Generating block with set difficulty");
            results.add(testBlockGeneration());
        }
        System.out.println(n + " blocks generated. Printing summary statistics");
        System.out.println(statistics(results));
    }

    private static String statistics(List<Long> data) {
        return data
                .stream()
                .mapToLong(x -> x)
                .summaryStatistics()
                .toString();
    }


    static boolean testFirstMerkel() {
        List<Integer> bar = Arrays.asList(1, 2, 3, 4, 5);
        MerkelTree<Integer> tree = new MerkelTree<>(bar);
        System.out.println("Serialized Bytes");
        byte[] foo = SerializationUtils.serialize(tree);
        System.out.println(foo);
        System.out.println("Deserializing the tree");
        MerkelTree<Integer> newTree = SerializationUtils.deserialize(foo);
        System.out.println(newTree.toString());
        return true;
    }

    static void testTransactionSerialization() {
        final Date ts = new Date();
        final String sender = "26 aa 1b eb 9a e2 7e ae b2 6e";
        final String receiver = "59 fa fd 9a 36 74 21 0a 14 fe";

        Transaction t = new Transaction(ts, sender, receiver, ScriptType.OP_INVD, 0);

        System.out.println("Printing Serialized Transaction");
        byte[] foo = SerializationUtils.serialize(t);
        System.out.println(foo);

        System.out.println("Printing Deserialized Transaction from serialized data");
        System.out.println(SerializationUtils.deserialize(foo).toString());
    }

    static Long testBlockGeneration() {
        final Date ts = new Date();
        final String sender = "26 aa 1b eb 9a e2 7e ae b2 6e";
        final String receiver = "59 fa fd 9a 36 74 21 0a 14 fe";

        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            transactions.add(new Transaction(ts, sender, receiver, ScriptType.OP_INVD, 0));
        }

        boolean blockSuccess = false;
        int counter = 0;
        Date startTime = new Date();
        while(!blockSuccess) {
            counter++;
            blockSuccess = tryBlock(counter, transactions);
        }
        Date endTime = new Date();
        System.out.println("Block generated after " + counter + " attempts.");
        System.out.println("Total Time: " + (endTime.getTime() - startTime.getTime()) + " ms");
        return endTime.getTime() - startTime.getTime();
    }

    private static boolean tryBlock(int nonce, List<Transaction> transactions) {
        BlockHeader header = new BlockHeader((short) 1, 1, getMerkelRoot(transactions), new Date(), 50000, nonce);
        Block b = new Block(transactions, header, (short) 30);

        if (b.hashCode() < header.getTarget())
            return true;
        else
            return false;
    }

    private static int getMerkelRoot(List<Transaction> in) {
        MerkelTree<Transaction> trans = new MerkelTree<>(in);
        return trans.getRoot();
    }

    private static int getBlockMerkelRoot(List<Block> in) {
        MerkelTree<Block> blks = new MerkelTree<>(in);
        return blks.getRoot();
    }
}
