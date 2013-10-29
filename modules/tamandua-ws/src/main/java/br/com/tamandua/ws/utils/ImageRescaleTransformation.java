/* Copyright (c) - UOL Inc,
 * Todos os direitos reservados
 *
 * Este arquivo e uma propriedade confidencial do Universo Online Inc.
 * Nenhuma parte do mesmo pode ser copiada, reproduzida, impressa ou
 * transmitida por qualquer meio sem autorizacao expressa e por escrito
 * de um representante legal do Universo Online Inc.
 *
 * All rights reserved
 *
 * This file is a confidential property of Universo Online Inc.
 * No part of this file may be reproduced or copied in any form or by
 * any means without written permisson from an authorized person from
 * Universo Online Inc.
 *
 * Historico de revisoes
 * Autor                             Data        Motivo
 * --------------------------------- ----------- -----------------------
 * Sandra Hagui                      20/08/2008  Criação da classe
 * Everton Rosario                   18/12/2008  Modificações/documentação
 */
package br.com.tamandua.ws.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * 
 * ImageRescaleTransformation
 * Utilitário para realizar o redimensionamento de imagens.
 *
 * @author Everton Rosario (sma_erosario@uolinc.com)
 * @author Sandra Hagui (sotsuki@uolinc.com)
 */
public class ImageRescaleTransformation {

    protected static final Logger logger = Logger
            .getLogger(ImageRescaleTransformation.class);

    static {
        ImageIO.setUseCache(false);
    }

    /**
     * @param in Streaming da imagem que será transformada.
     * @param sizeH Novo tamanho no eixo vertical para a imagem em pixels.
     * @param sizeW Novo tamanho no eixo horizontal para a imagem em pixels.
     * @return streaming da imagem transformada
     */
    public static InputStream getRescaleStream(InputStream in, int sizeH, int sizeW){
        BufferedImage image = null;
        try {
            image = ImageIO.read(in);
        } catch (Exception e) {
            logger.error("Ignorando imagem. Falha ao ler o streaming. MgsError: " + e.getMessage());
            return null;
        }

        if (image == null) {
        	logger.error("Ignorando imagem. Falha ao ler o streaming.");
            return null;
        }

        if (image.getWidth() == sizeW && image.getHeight() == sizeH) {
            return in;
        }

        if (image.getWidth() < sizeW || image.getHeight() < sizeH) {
            logger.warn("[IMAGEM OVERSIZED]: Imagem menor que tamanho desejado para resize: "
                    + " tamanho original("+image.getHeight()+"x"+image.getWidth()+") para (" + sizeW + "x" + sizeH+")");
        }

        logger.debug("alterando imagem: " + image.getWidth() + "x"
                + image.getHeight() + " para: " + sizeW + "x"
                + sizeH);

        BufferedImage img = getScaledInstance(image, sizeW, sizeH, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "jpg", out);
        } catch (Exception e) {
        	logger.error("Ignorando imagem. Falha ao escrever o streaming. MsgError: " + e.getMessage());
        	return null;
        }

        if(out != null){
        	return new ByteArrayInputStream(out.toByteArray());
        } else {
        	logger.error("Ignorando imagem. Falha ao escrever o streaming.");
        	return null;
        }
    }
    
    /**
     * @param from Arquivo de onde será feito a transformação
     * @param to Arquivo para o qual será armazenada a imagem redimensionada
     * @param sizeH Novo tamanho no eixo vertical para a imagem em pixels.
     * @param sizeW Novo tamanho no eixo horizontal para a imagem em pixels.
     * @throws IOException
     */
    public static void transform(File from, File to, int sizeH, int sizeW)
            throws IOException {
        to.mkdirs();

        BufferedImage image = null;
        try {
            image = ImageIO.read(from);
        } catch (Exception e) {
            logger.error("Ignorando imagem. Falha ao ler arquivo [" + from
                    + "] de imagem. MgsError: " + e.getMessage());
            return;
        }

        if (image == null) {
            logger.error("Ignorando imagem. Falha ao ler arquivo [" + from
                    + "] de imagem");
            return;
        }

        if (image.getWidth() == sizeW && image.getHeight() == sizeH) {
            FileUtils.copyFile(from, to);
            logger.debug("nao precisou alterar tamanho da imagem: " + from
                    + " " + sizeW + "x" + sizeH);
            return;
        }

        if (image.getWidth() < sizeW || image.getHeight() < sizeH) {
            FileUtils.copyFile(from, to);
            logger.warn("[IMAGEM OVERSIZED]: Imagem menor que tamanho desejado para resize: " + from
                    + " tamanho original("+image.getHeight()+"x"+image.getWidth()+") para (" + sizeW + "x" + sizeH+")");
        }

        logger.debug("alterando imagem: " + from + " " + image.getWidth() + "x"
                + image.getHeight() + " para: " + to + " " + sizeW + "x"
                + sizeH);

        BufferedImage img = getScaledInstance(image, sizeW, sizeH, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
        BufferedOutputStream out = null;
        try {
            ImageIO.write(img, "jpg", to);
        } catch (Exception e) {
            logger.error("Fail to write image. MsgError: " + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
        if (!to.exists())
            throw new IOException("arquivo de destino não criado " + to);
    }
    
    /**
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}.
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hints that corresponds to
     *    {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *    {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in downscaling cases, where
     *    {@code targetWidth} or {@code targetHeight} is
     *    smaller than the original dimensions, and generally only when
     *    the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code BufferedImage}
     */
    static public BufferedImage getScaledInstance(BufferedImage img,
                                           int targetWidth,
                                           int targetHeight,
                                           Object hint,
                                           boolean higherQuality)
    {
        int type = (img.getTransparency() == Transparency.OPAQUE) ?
            BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage)img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }
        
        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;                
            }
            if (w < targetWidth) {
                w = targetWidth;
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                
            }
            if (h < targetHeight) {
                h = targetHeight;
            }
            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }

}