package org.hsqldb.index;

import org.hsqldb.*;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.types.Type;

/**
 * Created by inocer on 11/14/17.
 */
public class IndexSPlayMemory extends IndexAVL{

    /**
     * Constructor declaration
     *
     * @param name       HsqlName of the index
     * @param id         persistnece id
     * @param table      table of the index
     * @param columns    array of column indexes
     * @param descending boolean[]
     * @param nullsLast  boolean[]
     * @param colTypes   array of column types
     * @param pk         if index is for a primary key
     * @param unique     is this a unique index
     * @param constraint does this index belonging to a constraint
     * @param forward    is this an auto-index for an FK that refers to a table
     */
    public IndexSPlayMemory(HsqlNameManager.HsqlName name, long id, TableBase table, int[] columns,
                            boolean[] descending, boolean[] nullsLast, Type[] colTypes, boolean pk,
                            boolean unique, boolean constraint, boolean forward) {
        super(name, id, table, columns, descending, nullsLast, colTypes, pk, unique, constraint, forward);
    }


    /**
     * Insert a node into the index
     */
    public void insert(Session session, PersistentStore store, Row row) {

        NodeAVL n;
        NodeAVL        root;
        boolean        isleft        = true;
        int            compare       = -1;
        final Object[] rowData       = row.getData();
        boolean        compareRowId  = !isUnique || hasNulls(session, rowData);
        boolean        compareSimple = isSimple;

        n = getAccessor(store);
        root = n;

        if (n == null) {
            store.setAccessor(this, ((RowAVL) row).getNode(position));
            return;
        }

//        while (true) {

            Row currentRow = n.row;
            compare = 0;

            if (compareSimple) {
                /**
                 * compare = 0 -----> if rowData == currentRow
                 * compare = 1 -----> if currentRow == null
                 *                       rowData > currentRow
                 * compare = -1 ----> if rowData == null
                 *                       rowData < currentRow
                 */
                compare =
                        colTypes[0].compare(session, rowData[colIndex[0]],
                                currentRow.getData()[colIndex[0]]);

                if (compare == 0 && compareRowId) {
                    compare = compareRowForInsertOrDelete(session, row,
                            currentRow,
                            compareRowId, 1);
                }
            } else {
                compare = compareRowForInsertOrDelete(session, row,
                        currentRow,
                        compareRowId, 0);
            }

            // after the first match and check, all compares are with row id
            if (compare == 0 && session != null && !compareRowId
                    && session.database.txManager.isMVRows()) {
                if (!isEqualReadable(session, store, n)) {
                    compareRowId = true;
                    compare = compareRowForInsertOrDelete(session, row,
                            currentRow,
                            compareRowId,
                            colIndex.length);
                }
            }

            if (compare == 0) {
                if (isConstraint) {
                    Constraint c =
                            ((Table) table).getUniqueConstraintForIndex(this);

                    throw c.getException(row.getData());
                } else {
                    throw Error.error(ErrorCode.X_23505, name.statementName);
                }
            }

//            isleft = compare < 0;
//            x      = n;
//            n      = isleft ? x.nLeft
//                    : x.nRight;
//
//            if (n == null) {
//                break;
//            }
//        }

        if (compare < 0){
            NodeAVL newNode = ((RowAVL) row).getNode(position);
            newNode.nLeft = root.nLeft;
            newNode.nRight = root;
            root.nLeft = null;
            store.setAccessor(this, newNode);
        }
        else if(compare >0){
            NodeAVL newNode = ((RowAVL) row).getNode(position);
            newNode.nRight = root.nRight;
            newNode.nLeft = root;
            root.nRight = null;
            store.setAccessor(this, newNode);
        }
//        root = root.set(store, isleft, ((RowAVL) row).getNode(position));
//
//        balance(store, x, isleft);

        // CASE WHERE compare = 0
    }


    void delete(PersistentStore store, NodeAVL x) {

        if (x == null) {
            return;
        }
        NodeAVL root = getAccessor(store);


    }


}

