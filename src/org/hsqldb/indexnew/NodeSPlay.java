package org.hsqldb.indexnew;

import org.hsqldb.Row;
import org.hsqldb.RowAVL;
import org.hsqldb.index.Index;
import org.hsqldb.lib.LongLookup;
import org.hsqldb.persist.CachedObject;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

/**
 * Created by inocer on 11/13/17.
 */
public class NodeSPlay implements CachedObject {

//    public int       iBalance;

    protected NodeSPlay nLeft;
    protected NodeSPlay nRight;
    protected NodeSPlay nParent;
    protected final Row row;

    NodeSPlay(){
        row = null;
    }

    public NodeSPlay(Row r){
        row = r;
    }

    public void delete(){
        nLeft = nRight = nParent = null;
    }

    NodeSPlay getLeft(PersistentStore store) {
        return nLeft;
    }

    NodeSPlay setLeft(PersistentStore persistentStore, NodeSPlay n) {
        nLeft = n;
        return this;
    }

    boolean isLeft(PersistentStore store, NodeSPlay node) {
        return nLeft == node;
    }

    boolean isRight(PersistentStore store, NodeSPlay node) {
        return nRight == node;
    }

    NodeSPlay getRight(PersistentStore persistentStore) {
        return nRight;
    }

    NodeSPlay setRight(PersistentStore persistentStore, NodeSPlay n) {

        nRight = n;

        return this;
    }

    NodeSPlay getParent(PersistentStore store) {
        return nParent;
    }

    boolean isRoot(PersistentStore store) {
        return nParent == null;
    }

    NodeSPlay setParent(PersistentStore persistentStore, NodeSPlay n) {

        nParent = n;

        return this;
    }

//    public NodeSPlay setBalance(PersistentStore store, int b) {
//
//        iBalance = b;
//
//        return this;
//    }

//    public int getBalance(PersistentStore store) {
//        return iBalance;
//    }

    boolean isFromLeft(PersistentStore store) {

        if (nParent == null) {
            return true;
        }

        return this == nParent.nLeft;
    }

    public NodeSPlay child(PersistentStore store, boolean isleft) {
        return isleft ? getLeft(store)
                : getRight(store);
    }

    public NodeSPlay set(PersistentStore store, boolean isLeft, NodeSPlay n) {

        if (isLeft) {
            nLeft = n;
        } else {
            nRight = n;
        }

        if (n != null) {
            n.nParent = this;
        }

        return this;
    }

    public void replace(PersistentStore store, Index index, NodeSPlay n) {

        if (nParent == null) {
            if (n != null) {
                n = n.setParent(store, null);
            }

            store.setAccessor(index, n);
        } else {
            nParent.set(store, isFromLeft(store), n);
        }
    }

    boolean equals(NodeSPlay n) {
        return n == this;
    }

    public RowAVL getRow(PersistentStore store) {
        return (RowAVL) row;
    }

    protected Object[] getData(PersistentStore store) {
        return row.getData();
    }


    @Override
    public boolean isMemory() {
        return true;
    }

    @Override
    public void updateAccessCount(int count) {

    }

    @Override
    public int getAccessCount() {
        return 0;
    }

    @Override
    public void setStorageSize(int size) {

    }

    @Override
    public int getStorageSize() {
        return 0;
    }

    @Override
    public boolean isInvariable() {
        return false;
    }

    @Override
    public boolean isBlock() {
        return false;
    }

    @Override
    public long getPos() {
        return 0;
    }

    @Override
    public void setPos(long pos) {

    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public boolean hasChanged() {
        return false;
    }

    @Override
    public void setChanged(boolean flag) {

    }

    @Override
    public boolean isKeepInMemory() {
        return false;
    }

    @Override
    public boolean keepInMemory(boolean keep) {
        return true;
    }

    @Override
    public boolean isInMemory() {
        return false;
    }

    @Override
    public void setInMemory(boolean in) {

    }

    @Override
    public void restore() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public int getRealSize(RowOutputInterface out) {
        return 0;
    }

    @Override
    public void read(RowInputInterface in) {

    }

    @Override
    public int getDefaultCapacity() {
        return 0;
    }

    @Override
    public void write(RowOutputInterface out) {

    }

    @Override
    public void write(RowOutputInterface out, LongLookup lookup) {

    }
}
