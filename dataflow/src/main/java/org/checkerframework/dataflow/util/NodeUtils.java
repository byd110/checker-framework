package org.checkerframework.dataflow.util;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;

import org.checkerframework.dataflow.cfg.node.BooleanLiteralNode;
import org.checkerframework.dataflow.cfg.node.ConditionalNotNode;
import org.checkerframework.dataflow.cfg.node.ConditionalOrNode;
import org.checkerframework.dataflow.cfg.node.FieldAccessNode;
import org.checkerframework.dataflow.cfg.node.MethodInvocationNode;
import org.checkerframework.dataflow.cfg.node.Node;
import org.checkerframework.dataflow.cfg.node.TypeCastNode;
import org.checkerframework.javacutil.ElementUtils;
import org.checkerframework.javacutil.TypesUtils;
import org.plumelib.util.CollectionsPlume;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;

/** A utility class to operate on a given {@link Node}. */
public class NodeUtils {

    /**
     * Returns true iff {@code node} corresponds to a boolean typed expression (either the primitive
     * type {@code boolean}, or class type {@link java.lang.Boolean}).
     *
     * @return true iff {@code node} corresponds to a boolean typed expression (either the primitive
     *     type {@code boolean}, or class type {@link java.lang.Boolean})
     */
    public static boolean isBooleanTypeNode(Node node) {

        if (node instanceof ConditionalOrNode) {
            return true;
        }

        // not all nodes have an associated tree, but those are all not of a boolean type.
        Tree tree = node.getTree();
        if (tree == null) {
            return false;
        }

        Type type = ((JCTree) tree).type;
        if (TypesUtils.isBooleanType(type)) {
            return true;
        }

        return false;
    }

    /**
     * Returns true iff {@code node} is a {@link FieldAccessNode} that is an access to an array's
     * length.
     *
     * @return true iff {@code node} is a {@link FieldAccessNode} that is an access to an array's
     *     length
     */
    public static boolean isArrayLengthFieldAccess(Node node) {
        if (!(node instanceof FieldAccessNode)) {
            return false;
        }
        FieldAccessNode fieldAccess = (FieldAccessNode) node;
        return fieldAccess.getFieldName().equals("length")
                && fieldAccess.getReceiver().getType().getKind() == TypeKind.ARRAY;
    }

    /**
     * Returns true if {@code node} is an invocation of the given method.
     *
     * @param node a node, not necessarily a method invocation
     * @param method a method
     * @param env a processing environment
     * @return true if {@code node} is an invocation of one of the given methods
     */
    public static boolean isMethodInvocation(
            Node node, ExecutableElement method, ProcessingEnvironment env) {
        if (!(node instanceof MethodInvocationNode)) {
            return false;
        }
        ExecutableElement invoked = ((MethodInvocationNode) node).getTarget().getMethod();
        return ElementUtils.isMethod(invoked, method, env);
    }

    /**
     * Returns true iff {@code node} is an invocation of one of the given methods.
     *
     * @param node a node, not necessarily a method invocation
     * @param methods a list of methods
     * @param env a processing environment
     * @return true if {@code node} is an invocation of one of the given methods
     */
    public static boolean isMethodInvocation(
            Node node, List<ExecutableElement> methods, ProcessingEnvironment env) {
        if (!(node instanceof MethodInvocationNode)) {
            return false;
        }
        ExecutableElement invoked = ((MethodInvocationNode) node).getTarget().getMethod();
        return CollectionsPlume.anyMatch(
                methods, method -> ElementUtils.isMethod(invoked, method, env));
    }

    /**
     * Returns true if the given node statically evaluates to {@code value} and has no side effects.
     *
     * @param n a node
     * @param value the boolean value that the node is tested against
     * @return true if the node is equivalent to a literal with value {@code value}
     */
    public static boolean isConstantBoolean(Node n, boolean value) {
        if (n instanceof BooleanLiteralNode) {
            return ((BooleanLiteralNode) n).getValue() == value;
        } else if (n instanceof ConditionalNotNode) {
            return isConstantBoolean(((ConditionalNotNode) n).getOperand(), !value);
        } else {
            return false;
        }
    }

    /**
     * Remove any {@link TypeCastNode}s wrapping a node, returning the operand nested within the
     * type casts.
     *
     * @param node a node
     * @return node, but with any surrounding typecasts removed
     */
    public static Node removeCasts(Node node) {
        while (node instanceof TypeCastNode) {
            node = ((TypeCastNode) node).getOperand();
        }
        return node;
    }
}
