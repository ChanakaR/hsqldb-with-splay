package org.hsqldb.indexnew;

import org.hsqldb.*;
import org.hsqldb.index.Index;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.navigator.RowIterator;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.rights.Grantee;
import org.hsqldb.types.Type;

/**
 * Created by inocer on 11/13/17.
 */
public class IndexSPlay implements Index {
    @Override
    public IndexUse[] asArray() {
        return new IndexUse[0];
    }

    @Override
    public RowIterator emptyIterator() {
        return null;
    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public void setPosition(int position) {

    }

    @Override
    public long getPersistenceId() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public boolean isConstraint() {
        return false;
    }

    @Override
    public int[] getColumns() {
        return new int[0];
    }

    @Override
    public Type[] getColumnTypes() {
        return new Type[0];
    }

    @Override
    public boolean[] getColumnDesc() {
        return new boolean[0];
    }

    @Override
    public int[] getDefaultColumnMap() {
        return new int[0];
    }

    @Override
    public int getIndexOrderValue() {
        return 0;
    }

    @Override
    public boolean isForward() {
        return false;
    }

    @Override
    public void setTable(TableBase table) {

    }

    @Override
    public TableBase getTable() {
        return null;
    }

    @Override
    public void setClustered(boolean clustered) {

    }

    @Override
    public boolean isClustered() {
        return false;
    }

    @Override
    public long size(Session session, PersistentStore store) {
        return 0;
    }

    @Override
    public long sizeUnique(PersistentStore store) {
        return 0;
    }

    @Override
    public double[] searchCost(Session session, PersistentStore store) {
        return new double[0];
    }

    @Override
    public long getNodeCount(Session session, PersistentStore store) {
        return 0;
    }

    @Override
    public boolean isEmpty(PersistentStore store) {
        return false;
    }

    @Override
    public int checkIndex(Session session, PersistentStore store) {
        return 0;
    }

    @Override
    public void insert(Session session, PersistentStore store, Row row) {

    }

    @Override
    public void delete(Session session, PersistentStore store, Row row) {

    }

    @Override
    public boolean existsParent(Session session, PersistentStore store, Object[] rowdata, int[] rowColMap) {
        return false;
    }

    @Override
    public RowIterator findFirstRow(Session session, PersistentStore store, Object[] rowdata, int matchCount, int distinctCount, int compareType, boolean reversed, boolean[] map) {
        return null;
    }

    @Override
    public RowIterator findFirstRow(Session session, PersistentStore store, Object[] rowdata) {
        return null;
    }

    @Override
    public RowIterator findFirstRow(Session session, PersistentStore store, Object[] rowdata, int[] rowColMap) {
        return null;
    }

    @Override
    public RowIterator findFirstRowNotNull(Session session, PersistentStore store) {
        return null;
    }

    @Override
    public RowIterator firstRow(PersistentStore store) {
        return null;
    }

    @Override
    public RowIterator firstRow(Session session, PersistentStore store, int distinctCount, boolean[] map) {
        return null;
    }

    @Override
    public RowIterator lastRow(Session session, PersistentStore store, int distinctCount, boolean[] map) {
        return null;
    }

    @Override
    public int compareRowNonUnique(Session session, Object[] a, Object[] b, int[] rowColMap) {
        return 0;
    }

    @Override
    public int compareRowNonUnique(Session session, Object[] a, Object[] b, int[] rowColMap, int fieldCount) {
        return 0;
    }

    @Override
    public int compareRowNonUnique(Session session, Object[] a, Object[] b, int fieldcount) {
        return 0;
    }

    @Override
    public int compareRow(Session session, Object[] a, Object[] b) {
        return 0;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public HsqlNameManager.HsqlName getName() {
        return null;
    }

    @Override
    public HsqlNameManager.HsqlName getSchemaName() {
        return null;
    }

    @Override
    public HsqlNameManager.HsqlName getCatalogName() {
        return null;
    }

    @Override
    public Grantee getOwner() {
        return null;
    }

    @Override
    public OrderedHashSet getReferences() {
        return null;
    }

    @Override
    public OrderedHashSet getComponents() {
        return null;
    }

    @Override
    public void compile(Session session, SchemaObject parentObject) {

    }

    @Override
    public String getSQL() {
        return null;
    }

    @Override
    public long getChangeTimestamp() {
        return 0;
    }



}
