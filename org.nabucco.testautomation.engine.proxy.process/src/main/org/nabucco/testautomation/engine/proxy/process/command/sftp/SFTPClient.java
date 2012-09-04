/*
 * Copyright 2012 PRODYNA AG
 *
 * Licensed under the Eclipse Public License (EPL), Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/eclipse-1.0.php or
 * http://www.nabucco.org/License.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nabucco.testautomation.engine.proxy.process.command.sftp;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.proxy.process.exeption.SFTPCommunicationException;
import org.nabucco.testautomation.engine.proxy.process.exeption.SFTPException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFTPClient
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class SFTPClient {

    private static final String SFTP = "sftp";

    private Session session;

    private ChannelSftp channel;

    private static NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(SFTPClient.class);

    /**
     * Connect the SFTP Client to the given server.
     * 
     * @param username
     *            username on the SFTP server
     * @param password
     *            password of the SFTP server user
     * @param host
     *            the hostname of the SFTP server
     * @param port
     *            the port of the SFTP server
     * 
     * @throws SFTPException
     *             when the connection cannot be established
     */
    public void connect(String username, String password, String host) throws SFTPException {
        this.connect(username, password, host, null);
    }

    /**
     * Connect the SFTP Client to the given server.
     * 
     * @param username
     *            username on the SFTP server
     * @param password
     *            password of the SFTP server user
     * @param host
     *            the hostname of the SFTP server
     * @param port
     *            the port of the SFTP server
     * 
     * @throws SFTPException
     *             when the connection cannot be established
     */
    public void connect(String username, String password, String host, Integer port) throws SFTPCommunicationException {
        JSch jSch = new JSch();

        try {
            if (port == null) {
                this.session = jSch.getSession(username, host);
            } else {
                this.session = jSch.getSession(username, host, port);
            }

            this.session.setUserInfo(new SFTPUserInfo());
            this.session.setPassword(password);
            this.session.connect();

            this.channel = (ChannelSftp) this.session.openChannel(SFTP);
            this.channel.connect();

        } catch (JSchException jse) {
            String address = username + "@" + host + ":" + port;
            throw new SFTPCommunicationException("Error creating SFTP Connection to '" + address + "'.", jse);
        }
    }

    /**
     * Checks whether the SFTP session is connected.
     * 
     * @return <b>true</b> if the SFTP session is connected, <b>false</b> if not
     */
    public boolean isConnected() {
        if (this.session == null) {
            return false;
        }
        if (!this.session.isConnected()) {
            return false;
        }
        if (this.channel == null) {
            return false;
        }
        return this.channel.isConnected();
    }

    /**
     * Disconnect the SFTP session.
     */
    public void disconnect() {
        if (this.channel != null && this.channel.isConnected()) {
            this.channel.disconnect();
        }
        if (this.session != null && this.session.isConnected()) {
            this.session.disconnect();
        }
    }

    /**
     * Change the current directory.
     * 
     * @param path
     *            the path to the new directory
     * 
     * @throws SFTPException
     *             when the directory cannot be changed
     */
    public void cd(String path) throws SFTPException {
        try {
            this.channel.cd(path);
        } catch (SftpException se) {
            logger.error(se, "Error changing SFTP directory to '", path, "'.");
            throw new SFTPException("Error changing SFTP directory to '" + path + "'.", se);
        }
    }

    /**
     * Create a new directory.
     * 
     * @param name
     *            the name of the new directory
     * 
     * @throws SFTPException
     *             when the directory cannot be created
     */
    public void mkdir(String name) throws SFTPException {
        try {
            this.channel.mkdir(name);
        } catch (SftpException se) {
            logger.error(se, "Error creating SFTP directory '", name, "'.");
            throw new SFTPException("Error creating SFTP directory '" + name + "'.", se);
        }
    }

    /**
     * List all files in the given directory.
     * 
     * @param path
     *            the path to the directory
     * 
     * @throws SFTPException
     *             when the directory does not exist or access is restricted
     */
    public List<LsEntry> ls(String path) throws SFTPException {
        try {
            @SuppressWarnings("unchecked")
            Vector<ChannelSftp.LsEntry> entries = this.channel.ls(path);
            return new ArrayList<ChannelSftp.LsEntry>(entries);
        } catch (SftpException se) {
            logger.error(se, "Error listing files in SFTP directory '", path, "'.");
            throw new SFTPException("Error listing files in SFTP directory '" + path + "'.", se);
        }
    }

    /**
     * Retrieve a file content as string from the SFTP server.
     * 
     * @param path
     *            the path of the file to retrieve
     * 
     * @return the file content as string
     * 
     * @throws SFTPException
     *             when the file cannot be loaded
     */
    public String get(String path) throws SFTPException {
        try {
            InputStream in = this.channel.get(path);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try {
                BufferedInputStream bin = new BufferedInputStream(in);
                int result = bin.read();

                while (result != -1) {
                    byte b = (byte) result;
                    out.write(b);
                    result = bin.read();
                }

                return out.toString();
            } finally {
                in.close();
                out.close();
            }

        } catch (SftpException se) {
            logger.error(se, "Error getting SFTP file '", path, "'.");
            throw new SFTPException("Error getting SFTP file '" + path + "'.", se);
        } catch (IOException ioe) {
            logger.error(ioe, "Error getting SFTP file '", path, "'.");
            throw new SFTPException("Error getting SFTP file '" + path + "'.", ioe);
        }
    }

    /**
     * Send a file as string to the SFTP server.
     * 
     * @param path
     *            the path of the file to send
     * @param content
     *            the file content as string
     * 
     * @throws SFTPException
     *             when the file cannot be loaded
     */
    public void get(String path, String content) throws SFTPException {
        try {
            OutputStream out = this.channel.put(path);

            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
                writer.write(content);

                out.write(content.getBytes());
            } finally {
                out.close();
            }

        } catch (SftpException se) {
            logger.error(se, "Error sending SFTP file '", path, "'.");
            throw new SFTPException("Error sending SFTP file '" + path + "'.", se);
        } catch (IOException ioe) {
            logger.error(ioe, "Error sending SFTP file '", path, "'.");
            throw new SFTPException("Error sending SFTP file '" + path + "'.", ioe);
        }
    }

    /**
     * Print the current working directory.
     * 
     * @return the path of the current working directory
     * 
     * @throws SFTPException
     *             when the working directory cannot be resolved
     */
    public String pwd() throws SFTPException {
        try {
            return this.channel.pwd();
        } catch (SftpException se) {
            logger.error(se, "Error printing current working directory.");
            throw new SFTPException("Error printing current working directory.", se);
        }
    }

    /**
     * Print the current local working directory.
     * 
     * @return the path of the current working directory
     * 
     * @throws SFTPException
     *             when the working directory cannot be resolved
     */
    public String lpwd() throws SFTPException {
        return this.channel.lpwd();
    }

    /**
     * Rename the old file to the new file.
     * 
     * @param oldPath
     *            path to the file to rename
     * @param newPath
     *            new file path
     * 
     * @throws SFTPException
     *             when the file cannot be renamed
     */
    public void rename(String oldPath, String newPath) throws SFTPException {
        try {
            this.channel.rename(oldPath, newPath);
        } catch (SftpException se) {
            logger.error(se, "Error renaming SFTP file from '", oldPath, "' to '", newPath, "'.");
            throw new SFTPException("Error renaming SFTP file from '" + oldPath + "' to '" + newPath + "'.", se);
        }
    }

    /**
     * Remove the given file (for directories see {@link SFTPClient#rmDir(String)}).
     * 
     * @param path
     *            the path to the file
     * 
     * @throws SFTPException
     *             when the file does not exist or access is restricted
     */
    public void rm(String path) throws SFTPException {
        try {
            this.channel.rm(path);
        } catch (SftpException se) {
            logger.error(se, "Error removing SFTP file '", path, "'.");
            throw new SFTPException("Error removing SFTP file '" + path + "'.", se);
        }
    }

    /**
     * Remove the given directory (for files see {@link SFTPClient#rm(String)}).
     * 
     * @param path
     *            the path to the directory
     * 
     * @throws SFTPException
     *             when the directory does not exist or access is restricted
     */
    public void rmDir(String path) throws SFTPException {
        try {
            this.channel.rmdir(path);
        } catch (SftpException se) {
            logger.error(se, "Error removing SFTP directory '", path, "'.");
            throw new SFTPException("Error removing SFTP directory '" + path + "'.", se);
        }
    }
}
