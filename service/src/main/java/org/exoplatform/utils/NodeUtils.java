package org.exoplatform.utils;

import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.service.exception.FunctionalConfigurationRuntimeException;
import org.exoplatform.services.wcm.core.NodeLocation;
import org.exoplatform.services.wcm.publication.WCMComposer;
import org.exoplatform.services.wcm.utils.WCMCoreUtils;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.HashMap;
import java.util.Objects;

public class NodeUtils {

    private final static String NODE_REPOSITORY = "repository";
    private final static String NODE_WORKSPACE = "collaboration";

    public static Node findCollaborationFile(String webContentUrl) {
        Node nodeByExpression = NodeLocation.getNodeByLocation(new NodeLocation(NODE_REPOSITORY, NODE_WORKSPACE, webContentUrl, null,true));
        if (Objects.isNull(nodeByExpression)) {
            throw new FunctionalConfigurationRuntimeException("functionalConfiguration.termsAndConditions.fileNotFound");
        }
        return nodeByExpression;
    }

    public static String getNodePath(String webContentUrl) {
        Node node = findCollaborationFile(webContentUrl);

        try {
            String encodedPath =  node.getPath();
            return "/" + CommonsUtils.getRestContextName() + "/private/jcr/repository/collaboration" + encodedPath;
        } catch (Exception e) {
            throw new FunctionalConfigurationRuntimeException("termsAndConditions.fileNotFound");
        }
    }





    public static String getWebContentContentFromUrl(String webContentUrl) {
        try {
            Node node = NodeUtils.findCollaborationFile(webContentUrl);
            NodeLocation nodeLocationByNode = NodeLocation.getNodeLocationByNode(node);

            Node content = WCMCoreUtils.getService(WCMComposer.class)
                    .getContent(nodeLocationByNode.getWorkspace(),
                            nodeLocationByNode.getPath(),
                            new HashMap<>(),
                            WCMCoreUtils.getSystemSessionProvider());


            return content.getNode("default.html/jcr:content").getProperty("jcr:data").getString();
        } catch (Exception e) {
            throw new FunctionalConfigurationRuntimeException(e.getMessage());
        }
    }
}
