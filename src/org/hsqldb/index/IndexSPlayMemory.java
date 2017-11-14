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
        // CASE WHERE compare = 0
    }


    public void delete(Session session, PersistentStore store, Row row) {

        NodeAVL keyNode = ((RowAVL)row).getNode(position);
        if ( keyNode == null) {
            return;
        }

        NodeAVL root = getAccessor(store);

        root = splay(session,store,root, keyNode);

        int cmp = this.compare(session,store,root,keyNode);

        if (cmp == 0) {
            if (root.nLeft == null) {
                root = root.nRight;
            }
            else {
                NodeAVL x = root.nRight;
                root = root.nLeft;
                splay(session,store,root,keyNode);
                root.nRight = x;
            }
        }
    }

    /***************************************************************************
     * Splay tree function.
     * **********************************************************************/
    // splay key in the tree rooted at Node h. If a node with that key exists,
    //   it is splayed to the root of the tree. If it does not, the last node
    //   along the search path for the key is splayed to the root.

    private NodeAVL splay(Session session,PersistentStore store,NodeAVL h, NodeAVL key) {

        boolean        compareSimple = isSimple;
        int            compare       = -1;
        final Object[] rowData       = key.row.getData();
        boolean        compareRowId  = !isUnique || hasNulls(session, rowData);

        if (h == null) return null;

        int compare1 = this.compare(session,store,h,key);

        if (compare1 < 0) {
            // key not in tree, so we're done
            if (h.nLeft == null) {
                return h;
            }
            int cmp2 = this.compare(session,store,h.nLeft,key);
            if (cmp2 < 0) {
                h.nLeft.nLeft= splay(session,store,h.nLeft.nLeft, key);
                h = rotateRight(h);
            }
            else if (cmp2 > 0) {
                h.nLeft.nRight = splay(session,store,h.nLeft.nRight, key);
                if (h.nLeft.nRight!= null)
                    h.nLeft = rotateLeft(h.nLeft);
            }

            if (h.nLeft == null) return h;
            else                return rotateRight(h);
        }

        else if (compare1 > 0) {
            // key not in tree, so we're done
            if (h.nRight == null) {
                return h;
            }

            int cmp2 = this.compare(session,store,h.nRight,key);
            if (cmp2 < 0) {
                h.nRight.nLeft= splay(session,store,h.nRight.nLeft, key);
                if (h.nRight.nLeft!= null)
                    h.nRight = rotateRight(h.nRight);
            }
            else if (cmp2 > 0) {
                h.nRight.nRight= splay(session,store,h.nRight.nRight, key);
                h = rotateLeft(h);
            }

            if (h.nRight == null) return h;
            else                 return rotateLeft(h);
        }

        else return h;
    }

    private int compare(Session session,PersistentStore store,NodeAVL h, NodeAVL key){
        boolean        compareSimple = isSimple;
        int            compare       = -1;
        final Object[] rowData       = key.row.getData();
        boolean        compareRowId  = !isUnique || hasNulls(session, rowData);

        if (compareSimple) {
            /**
             * compare = 0 -----> if rowData == currentRow
             * compare = 1 -----> if currentRow == null
             *                       rowData > currentRow
             * compare = -1 ----> if rowData == null
             *                       rowData < currentRow
             */

            compare =
                    colTypes[0].compare(session, key.row.getData()[colIndex[0]],
                            h.row.getData()[colIndex[0]]);

            if (compare == 0 && compareRowId) {
                compare = compareRowForInsertOrDelete(session, key.row,
                        h.row,
                        compareRowId, 1);
            }
        } else {
            compare = compareRowForInsertOrDelete(session, key.row,
                    h.row,
                    compareRowId, 0);
        }

        // after the first match and check, all compares are with row id
        if (compare == 0 && session != null && !compareRowId
                && session.database.txManager.isMVRows()) {
            if (!isEqualReadable(session, store, h)) {
                compareRowId = true;
                compare = compareRowForInsertOrDelete(session, key.row,
                        h.row,
                        compareRowId,
                        colIndex.length);
            }
        }

        return compare;
    }

    // right rotate
    private NodeAVL rotateRight(NodeAVL h) {
        NodeAVL x = h.nLeft;
        h.nLeft = x.nRight;
        x.nRight = h;
        return x;
    }

    // left rotate
    private NodeAVL rotateLeft(NodeAVL h) {
        NodeAVL x = h.nRight;
        h.nRight = x.nLeft;
        x.nLeft = h;
        return x;
    }

    /***************************************************************************
     *  Helper functions.
     ***************************************************************************/

    // height of tree (1-node tree has height 0)
//    public int height() { return height(root); }
//    private int height(NodeAVL x) {
//        if (x == null) return -1;
//        return 1 + Math.max(height(x.nLeft), height(x.nRight));
//    }
//
//
//    public int size() {
//        return size(root);
//    }
//
//    private int size(NodeAVL x) {
//        if (x == null) return 0;
//        else return 1 + size(x.nLeft) + size(x.nRight);
//    }




}

