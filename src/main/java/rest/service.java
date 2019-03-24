package rest;

import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import sun.misc.IOUtils;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("/service")
public class service {
    // Available image extensions
    List<String> imagesAvailableExtensios = new ArrayList<String>(Arrays.asList("png", "jpg", "bmp", "tif", "tiff", "jpeg", "gif"));

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/validateImage")
    public Response validateImage(
            @FormDataParam("image") InputStream uploadedInputStream,
            @FormDataParam("image") FormDataContentDisposition fileDetail) throws IOException {

        // check if all form parameters are provided
        if (uploadedInputStream == null || fileDetail == null)
            return Response.status(400).entity("Invalid form data").build();
        // check extensions
        String extensionImage = FilenameUtils.getExtension(fileDetail.getFileName());
        if (!imagesAvailableExtensios.contains(extensionImage)) {
            return Response.status(500)
                    .entity("Error File Not Image")
                    .build();
        }
        String uploadFolder = System.getProperty("user.home") + File.separator + "uploads";

        // create our destination folder, if it not exists
        try {
            createFolderIfNotExists(uploadFolder);


        } catch (SecurityException se) {
            return Response.status(500)
                    .entity("Can not create destination folder on server")
                    .build();
        }
        // file location
        String uploadedFileLocation = uploadFolder + File.separator + fileDetail.getFileName();


        try {
            saveToFile(uploadedInputStream, uploadedFileLocation);
            File file = new File(uploadedFileLocation);
            // check Image Size < .5mb
            if (file.length() > (1024 * 1024) / 2) {
                // return to previous page

                return Response.status(400)
                        .entity("Image Size must < .5mb")
                        .build();

            }

            // check image width < 280 and height < 250 (Dimensions)
            Integer[] imageDimension = getImageDimensions(file);
            if (imageDimension[0] > 280 || imageDimension[1] > 250) {
                // return to previous page
                return Response.status(400)
                        .entity("Image Dimensions must { width < 280 and height < 250 }")
                        .build();

            }
        } catch (IOException e) {
            return Response.status(500).entity("Can not save file").build();
        }
        return Response.status(200)
                .entity("Image Valid ").build();


    }

    /**
     * Utility method to save InputStream data to target location/file
     *
     * @param inStream - InputStream to be saved
     * @param target   - full path to destination file
     */
    private void saveToFile(InputStream inStream, String target)
            throws IOException {
        OutputStream out = null;
        int read = 0;
        byte[] bytes = new byte[1024];
        out = new FileOutputStream(new File(target));
        while ((read = inStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();

    }

    /**
     * Creates a folder to desired location if it not already exists
     *
     * @param dirName - full path to the folder
     * @throws SecurityException - in case you don't have permission to create the folder
     */
    private void createFolderIfNotExists(String dirName)
            throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }

    public Integer[] getImageDimensions(File file) throws IOException {
        BufferedImage bimg = ImageIO.read(file);
        int width = bimg.getWidth();
        int height = bimg.getHeight();
        return new Integer[]{width, height};
    }
}
