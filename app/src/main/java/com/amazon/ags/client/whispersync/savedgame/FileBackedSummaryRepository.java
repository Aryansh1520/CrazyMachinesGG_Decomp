package com.amazon.ags.client.whispersync.savedgame;

import android.content.Context;
import android.util.Log;
import com.amazon.ags.client.whispersync.GameSummary;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

/* loaded from: classes.dex */
public class FileBackedSummaryRepository implements SummaryRepository {
    public static final String AMAZON_GAMES_DIR_NAME = ".amazonGamesService";
    private static final String PENDING_DOWNLOAD_DATA_FILE_NAME = "pendingDownloadData.dat";
    private static final String PENDING_DOWNLOAD_SUMMARY_FILE_NAME = "pendingDownloadSummary.dat";
    private static final String PENDING_UPLOAD_DATA_FILE_NAME = "pendingUploadData.dat";
    private static final String PENDING_UPLOAD_DESCRIPTION_FILE_NAME = "pendingUploadDescription.txt";
    private static final String SUMMARY_FILE_NAME = "latestSummary.txt";
    private static final String TAG = "AG_WS_" + FileBackedSummaryRepository.class.getSimpleName();
    private final Context context;
    private final SummaryMarshaller marshaller;

    public FileBackedSummaryRepository(Context context, SummaryMarshaller marshaller) {
        this.context = context;
        this.marshaller = marshaller;
    }

    @Override // com.amazon.ags.client.whispersync.savedgame.SummaryRepository
    public final void removeSummary() {
        File summaryFile = new File(getAmazonGamesDir(), SUMMARY_FILE_NAME);
        summaryFile.delete();
    }

    @Override // com.amazon.ags.client.whispersync.savedgame.SummaryRepository
    public final GameSummary retrieveSummary() {
        return readGameSummary(SUMMARY_FILE_NAME);
    }

    @Override // com.amazon.ags.client.whispersync.savedgame.SummaryRepository
    public final void storeSummary(GameSummary summary) {
        writeGameSummary(summary, SUMMARY_FILE_NAME);
    }

    @Override // com.amazon.ags.client.whispersync.savedgame.SummaryRepository
    public final void storePendingUpload(PendingUpload pendingUpload) throws IOException {
        writeToAmazonGamesDir(PENDING_UPLOAD_DATA_FILE_NAME, pendingUpload.getData());
        if (pendingUpload.getDescription() == null) {
            new File(getAmazonGamesDir(), PENDING_UPLOAD_DESCRIPTION_FILE_NAME).delete();
        } else {
            writeToAmazonGamesDir(PENDING_UPLOAD_DESCRIPTION_FILE_NAME, pendingUpload.getDescription().getBytes());
        }
    }

    @Override // com.amazon.ags.client.whispersync.savedgame.SummaryRepository
    public final PendingUpload retrievePendingUpload() throws IOException {
        byte[] gameData = readFromAmazonGamesDir(PENDING_UPLOAD_DATA_FILE_NAME);
        byte[] descriptionBytes = readFromAmazonGamesDir(PENDING_UPLOAD_DESCRIPTION_FILE_NAME);
        String description = descriptionBytes == null ? null : new String(descriptionBytes);
        Date saveDate = lastModified(PENDING_UPLOAD_DATA_FILE_NAME);
        return new PendingUpload(gameData, description, saveDate);
    }

    @Override // com.amazon.ags.client.whispersync.savedgame.SummaryRepository
    public final void removePendingUpload() {
        File pendingGameDataFile = new File(getAmazonGamesDir(), PENDING_UPLOAD_DATA_FILE_NAME);
        File pendingGameDescription = new File(getAmazonGamesDir(), PENDING_UPLOAD_DESCRIPTION_FILE_NAME);
        pendingGameDataFile.delete();
        pendingGameDescription.delete();
    }

    @Override // com.amazon.ags.client.whispersync.savedgame.SummaryRepository
    public final void storePendingDownload(PendingDownload pendingDownload) throws IOException {
        writeToAmazonGamesDir(PENDING_DOWNLOAD_DATA_FILE_NAME, pendingDownload.getData());
        writeGameSummary(pendingDownload.getSummary(), PENDING_DOWNLOAD_SUMMARY_FILE_NAME);
    }

    @Override // com.amazon.ags.client.whispersync.savedgame.SummaryRepository
    public final boolean hasPendingDownload() {
        return new File(getAmazonGamesDir(), PENDING_DOWNLOAD_DATA_FILE_NAME).exists();
    }

    @Override // com.amazon.ags.client.whispersync.savedgame.SummaryRepository
    public final PendingDownload retrievePendingDownload() throws IOException {
        byte[] gameData = readFromAmazonGamesDir(PENDING_DOWNLOAD_DATA_FILE_NAME);
        GameSummary summary = readGameSummary(PENDING_DOWNLOAD_SUMMARY_FILE_NAME);
        if (gameData == null) {
            return null;
        }
        return new PendingDownload(gameData, summary);
    }

    @Override // com.amazon.ags.client.whispersync.savedgame.SummaryRepository
    public final void removePendingDownload() {
        new File(getAmazonGamesDir(), PENDING_DOWNLOAD_DATA_FILE_NAME).delete();
        new File(getAmazonGamesDir(), PENDING_DOWNLOAD_SUMMARY_FILE_NAME).delete();
    }

    private GameSummary readGameSummary(String fileName) {
        File summaryFile = new File(getAmazonGamesDir(), fileName);
        try {
            FileReader fr = new FileReader(summaryFile);
            try {
                BufferedReader br = new BufferedReader(fr);
                String jsonSummary = br.readLine();
                return this.marshaller.unmarshal(jsonSummary);
            } finally {
                fr.close();
            }
        } catch (IOException e) {
            return null;
        }
    }

    private void writeGameSummary(GameSummary summary, String fileName) {
        String jsonSummary = this.marshaller.marshal(summary);
        if (jsonSummary != null) {
            try {
                writeToAmazonGamesDir(fileName, jsonSummary.getBytes());
            } catch (IOException e) {
                Log.e(TAG, "Could not write game summary", e);
            }
        }
    }

    private byte[] readFromAmazonGamesDir(String fileName) throws IOException {
        File sourceFile = new File(getAmazonGamesDir(), fileName);
        if (!sourceFile.exists()) {
            return null;
        }
        RandomAccessFile file = new RandomAccessFile(sourceFile, "r");
        try {
            byte[] gameData = new byte[(int) file.length()];
            file.readFully(gameData);
            return gameData;
        } finally {
            file.close();
        }
    }

    private void writeToAmazonGamesDir(String fileName, byte[] data) throws IOException {
        File targetFile = new File(getAmazonGamesDir(), fileName);
        FileOutputStream fos = new FileOutputStream(targetFile);
        try {
            fos.write(data);
        } finally {
            fos.close();
        }
    }

    private Date lastModified(String fileName) {
        File file = new File(getAmazonGamesDir(), fileName);
        if (file.exists()) {
            return new Date(file.lastModified());
        }
        return null;
    }

    private File getAmazonGamesDir() {
        File dir = new File(this.context.getFilesDir().getParentFile(), AMAZON_GAMES_DIR_NAME);
        dir.mkdirs();
        return dir;
    }
}
