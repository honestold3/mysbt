package org.wq.mysbt.cassandra;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

import java.net.InetAddress;
import java.util.LinkedList;



/**
 * Created by wq on 14/11/24.
 */
public class CassandraInsert  {

    public static Integer num = 50000;

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        try {
            for(int i= 0;i<1; i++) {
                insert(i);
                System.out.println(i + " echo time:"+ (System.currentTimeMillis() - start));
                //del();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("total time is:" +(System.currentTimeMillis() - start));
    }

    public static void insert(int index) throws Exception{
        String keyspace = "demodb";
        //Cluster cluster = Cluster.builder().addContactPoint("localhost").build();

        java.util.Collection<java.net.InetAddress> collection = new LinkedList();

        //collection.add(InetAddress.getByName("cloud203"));
        //collection.add(InetAddress.getByName("cloud204"));
//        collection.add(InetAddress.getByName("cloud205"));
//        collection.add(InetAddress.getByName("cloud206"));
//        collection.add(InetAddress.getByName("cloud207"));
//        collection.add(InetAddress.getByName("cloud208"));
//        collection.add(InetAddress.getByName("cloud209"));
        collection.add(InetAddress.getByName("cloud210"));

        //InetAddress.getByName("cloud203");

        Cluster cluster = Cluster.builder().addContactPoints(collection).build();

        Session session=cluster.connect(keyspace);

        BatchStatement batch = new BatchStatement();

        PreparedStatement ps = session.prepare("INSERT INTO users (user_id,  fname, lname) VALUES (?, ?, ?)");
        for (int i = 0; i < num; i++) {
//            LogEntry log = new LogEntry();
//            log.setPartitionId(TimeSliceUtil.getCurrentSlice());
//            log.setAppType("test");
//            log.setContent(System.currentTimeMillis() +" ");
//            log.setHost("10.0.250.121");
//            log.setUuid(UUID.randomUUID().toString());
//
//            BoundStatement bs = ps.bind(log.getPartitionId(), log.getUuid(),
//            log.getHost(), log.getAppType(), log.getContent());
//            batch.add(bs);

            User user = new User();
            user.setUser_id((index*100000)+i);
            user.setFname("fname"+Integer.toString(i));
            user.setLname("lname");
            BoundStatement bs = ps.bind(user.getUser_id(),user.getFname(),user.getLname());
            batch.add(bs);
        }
        session.execute(batch);

        batch.clear();
    }

    public static void del() throws Exception{
        String keyspace = "demodb";
        java.util.Collection<java.net.InetAddress> collection = new LinkedList();

//        collection.add(InetAddress.getByName("cloud203"));
//        collection.add(InetAddress.getByName("cloud204"));
//        collection.add(InetAddress.getByName("cloud205"));
//        collection.add(InetAddress.getByName("cloud206"));
//        collection.add(InetAddress.getByName("cloud207"));
//        collection.add(InetAddress.getByName("cloud208"));
//        collection.add(InetAddress.getByName("cloud209"));
        collection.add(InetAddress.getByName("cloud210"));

        //InetAddress.getByName("cloud203");

        Cluster cluster = Cluster.builder().addContactPoints(collection).build();

        Session session=cluster.connect(keyspace);

        BatchStatement batch = new BatchStatement();

        PreparedStatement ps = session.prepare("delete from users where user_id = ?");
        for (int i = 0; i < num; i++) {
//            LogEntry log = new LogEntry();
//            log.setPartitionId(TimeSliceUtil.getCurrentSlice());
//            log.setAppType("test");
//            log.setContent(System.currentTimeMillis() +" ");
//            log.setHost("10.0.250.121");
//            log.setUuid(UUID.randomUUID().toString());
//
//            BoundStatement bs = ps.bind(log.getPartitionId(), log.getUuid(),
//            log.getHost(), log.getAppType(), log.getContent());
//            batch.add(bs);

            User user = new User();
            user.setUser_id(i);
            //user.setFname("fname"+Integer.toString(i));
            //user.setLname("lname");
            BoundStatement bs = ps.bind(user.getUser_id());
            batch.add(bs);
        }
        session.execute(batch);

        batch.clear();

    }


}
