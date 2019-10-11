package com.bigfong.spring.framework.webmvc.server;

import com.bigfong.spring.framework.webmvc.servlet.TestServlet;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.core.StandardService;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.coyote.AbstractProtocol;
import org.apache.tomcat.util.file.ConfigFileLoader;
import org.apache.tomcat.util.res.StringManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyTomcat2 {
    private static final StringManager sm = StringManager.getManager(Tomcat.class);

    private String protocol = "org.apache.coyote.http11.Http11NioProtocol";
    private int port = 8081;
    private Charset uriEncoding = StandardCharsets.UTF_8;
    protected Server server;
    protected String basedir;
    private int backgroundProcessorDelay=10;
    private List<Connector> additionalTomcatConnectors = new ArrayList();

    public int getPort() {
        return this.port;
    }

    public void setUriEncoding(Charset uriEncoding) {
        this.uriEncoding = uriEncoding;
    }

    public Charset getUriEncoding() {
        return this.uriEncoding;
    }

    public void setBaseDir(String basedir) {
        this.basedir = basedir;
    }

    public void getWebServer() throws Exception {
        Tomcat tomcat = new Tomcat();
        File baseDir = this.createTempDir("tomcat");
        tomcat.setBaseDir(baseDir.getAbsolutePath());
        Connector connector = new Connector(this.protocol);
        tomcat.getService().addConnector(connector);
       // this.customizeConnector(connector);
        tomcat.setConnector(connector);
        tomcat.getHost().setAutoDeploy(false);
        //this.configureEngine(tomcat.getEngine());
        Iterator var5 = this.additionalTomcatConnectors.iterator();

        while (var5.hasNext()) {
            Connector additionalConnector = (Connector) var5.next();
            tomcat.getService().addConnector(additionalConnector);
        }

        this.prepareContext(tomcat.getHost(), initializers);
        return this.getTomcatWebServer(tomcat);
    }


    protected final File createTempDir(String prefix) {
        try {
            File tempDir = File.createTempFile(prefix + ".", "." + this.getPort());
            tempDir.delete();
            tempDir.mkdir();
            tempDir.deleteOnExit();
            return tempDir;
        } catch (IOException var3) {
            throw new RuntimeException("Unable to create tempDir. java.io.tmpdir is set to " + System.getProperty("java.io.tmpdir"), var3);
        }
    }
    /*public void setConnector(Connector connector) {
        Service service = this.getService();
        boolean found = false;
        Connector[] var4 = service.findConnectors();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Connector serviceConnector = var4[var6];
            if (connector == serviceConnector) {
                found = true;
            }
        }

        if (!found) {
            service.addConnector(connector);
        }

    }
    public Service getService() {
        return this.getServer().findServices()[0];
    }
    public Server getServer() {
        if (this.server != null) {
            return this.server;
        } else {
            System.setProperty("catalina.useNaming", "false");
            this.server = new StandardServer();
            this.initBaseDir();
            ConfigFileLoader.setSource(new CatalinaBaseConfigurationSource(new File(this.basedir), (String)null));
            this.server.setPort(-1);
            Service service = new StandardService();
            service.setName("Tomcat");
            this.server.addService(service);
            return this.server;
        }
    }*/

    protected void initBaseDir() {
        String catalinaHome = System.getProperty("catalina.home");
        if (this.basedir == null) {
            this.basedir = System.getProperty("catalina.base");
        }

        if (this.basedir == null) {
            this.basedir = catalinaHome;
        }

        if (this.basedir == null) {
            this.basedir = System.getProperty("user.dir") + "/tomcat." + this.port;
        }

        File baseFile = new File(this.basedir);
        if (baseFile.exists()) {
            if (!baseFile.isDirectory()) {
                throw new IllegalArgumentException(sm.getString("tomcat.baseDirNotDir", new Object[]{baseFile}));
            }
        } else if (!baseFile.mkdirs()) {
            throw new IllegalStateException(sm.getString("tomcat.baseDirMakeFail", new Object[]{baseFile}));
        }

        try {
            baseFile = baseFile.getCanonicalFile();
        } catch (IOException var6) {
            baseFile = baseFile.getAbsoluteFile();
        }

        this.server.setCatalinaBase(baseFile);
        System.setProperty("catalina.base", baseFile.getPath());
        this.basedir = baseFile.getPath();
        if (catalinaHome == null) {
            this.server.setCatalinaHome(baseFile);
        } else {
            File homeFile = new File(catalinaHome);
            if (!homeFile.isDirectory() && !homeFile.mkdirs()) {
                throw new IllegalStateException(sm.getString("tomcat.homeDirMakeFail", new Object[]{homeFile}));
            }

            try {
                homeFile = homeFile.getCanonicalFile();
            } catch (IOException var5) {
                homeFile = homeFile.getAbsoluteFile();
            }

            this.server.setCatalinaHome(homeFile);
        }

        System.setProperty("catalina.home", this.server.getCatalinaHome().getPath());
    }
}