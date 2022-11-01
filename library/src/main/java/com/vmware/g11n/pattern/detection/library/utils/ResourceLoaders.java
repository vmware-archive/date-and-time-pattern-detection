/*
 * Copyright 2022 VMware, Inc.
 * SPDX-License-Identifier: MIT
 */

package com.vmware.g11n.pattern.detection.library.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.vmware.g11n.pattern.detection.library.data.CldrMappers;
import com.vmware.g11n.pattern.detection.library.exceptions.DateTimePatternDetectionException;
import com.vmware.g11n.pattern.detection.model.CldrData;
import com.vmware.g11n.pattern.detection.model.calendar.GregorianCalendar;
import com.vmware.g11n.pattern.detection.model.dateFields.DateFields;
import com.vmware.g11n.pattern.detection.model.timezones.TimezoneNames;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.vmware.g11n.pattern.detection.library.data.PatternDetectionConstants.NOT_SUPPORTED_LOCALE_ERROR;

public class ResourceLoaders {

    // We need this to cover the cases where the standard locale contains different language and region code
    // Example: Standard for Portuguese is pt-BR, not pt-PT. Standard for English is en-US, not en-EN
    static ImmutableMap<String, String> standardLocalesToCldrNamingLocales = ImmutableMap.<String, String>builder()
            .put("en-US", "en")
            .put("pt-BR", "pt")
            .put("ko-KR", "ko")
            .put("ja-JP", "ja")
            .put("zh-CN", "zh-Hans")
            .put("zh-TW", "zh-Hant")
            .build();

    public void generateCldrData(String cldrVersion, List<String> locales) {
        try {
            downloadAndGenerateCldrJsonResources(cldrVersion, locales);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveYamlToFile(final Object object) {
        final DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        final Yaml yaml = new Yaml(options);

        final FileWriter writer;
        try {
            writer = new FileWriter("library/src/main/resources/application.yml");
            yaml.dump(object, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CldrData loadCldrData(Locale locale) {
        return CldrData.builder()
                .gregorianCalendar(getGregorianCalendarFromResources(locale))
                .dateFields(getDateFieldsFromResources(locale))
                .timezoneNames(getCldrDateTimezoneFields(locale))
                .build();
    }

    private static GregorianCalendar getGregorianCalendarFromResources(Locale locale) {
        ObjectMapper mapper = new ObjectMapper();

        String[] components = locale.toLanguageTag().split("-", 2);
        String language = components[0];
        String region = components[1];

        GregorianCalendar calendar;
        try {
            calendar = mapper.readValue(CldrMappers.class.getClassLoader()
                    .getResource("cldr/" + language + "/" + region + "/dates-full.json"), GregorianCalendar.class);
        } catch (IOException | IllegalArgumentException e) {
            throw new DateTimePatternDetectionException(NOT_SUPPORTED_LOCALE_ERROR);
        }
        return calendar;
    }

    private static DateFields getDateFieldsFromResources(Locale locale) {
        ObjectMapper mapper = new ObjectMapper();

        String[] components = locale.toLanguageTag().split("-", 2);
        String language = components[0];
        String region = components[1];

        DateFields fields;
        try {
            fields = mapper.readValue(CldrMappers.class.getClassLoader()
                    .getResource("cldr/" + language + "/" + region + "/date-fields.json"), DateFields.class);
        } catch (IOException e) {
            throw new DateTimePatternDetectionException("Can't load CLDR Date Field data.");
        }
        return fields;
    }

    private static TimezoneNames getCldrDateTimezoneFields(Locale locale) {
        ObjectMapper mapper = new ObjectMapper();

        String[] components = locale.toLanguageTag().split("-", 2);
        String language = components[0];
        String region = components[1];

        TimezoneNames timezones;
        try {
            timezones = mapper.readValue(CldrMappers.class.getClassLoader()
                    .getResource("cldr/" + language + "/" + region + "/timezone-fields.json"), TimezoneNames.class);
        } catch (IOException e) {
            throw new DateTimePatternDetectionException("Can't load CLDR Date Field data.");
        }
        return timezones;
    }

    private static void generateCalendarDatesJson(String path, List<String> locales) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        for (String locale : locales) {
            String localeDirectory = loadCldrDirectoryLocale(locale);

            JSONObject cldrFullDateFieldsAsJson = new JSONObject(IOUtils.toString(new FileInputStream(path + "/cldr-dates-full/main/"
                    + localeDirectory + "/ca-gregorian.json"))).getJSONObject("main").getJSONObject(localeDirectory)
                    .getJSONObject("dates").getJSONObject("calendars").getJSONObject("gregorian");

            String dirPath = createJsonResourceDirectory(locale);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(dirPath + "dates-full.json"),
                    mapper.readValue(cldrFullDateFieldsAsJson.toString(), GregorianCalendar.class));
        }
    }

    private static void generateDateFields(String path, List<String> locales) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        for (String locale : locales) {
            String localeDirectory = loadCldrDirectoryLocale(locale);

            JSONObject cldrFullDateFieldsAsJson = new JSONObject(IOUtils.toString(new FileInputStream(path + "/cldr-dates-full/main/"
                    + localeDirectory + "/dateFields.json"))).getJSONObject("main").getJSONObject(localeDirectory)
                    .getJSONObject("dates").getJSONObject("fields");

            String dirPath = createJsonResourceDirectory(locale);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(dirPath + "date-fields.json"),
                    mapper.readValue(cldrFullDateFieldsAsJson.toString(), DateFields.class));
        }
    }

    private static void generateTimezoneJson(String path, List<String> locales) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        for (String locale : locales) {
            String localeDirectory = loadCldrDirectoryLocale(locale);

            JSONObject cldrFullDateFieldsAsJson = new JSONObject(IOUtils.toString(new FileInputStream(path + "/cldr-dates-full/main/"
                    + localeDirectory + "/timeZoneNames.json"))).getJSONObject("main").getJSONObject(localeDirectory)
                    .getJSONObject("dates").getJSONObject("timeZoneNames");

            String dirPath = createJsonResourceDirectory(locale);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(dirPath + "timezone-fields.json"),
                    mapper.readValue(cldrFullDateFieldsAsJson.toString(), TimezoneNames.class));
        }
    }

    /*
    By default, the downloaded CLDR zip hosts all Json data in locale-specific directories. Some of those repositories
    are named with locale name + equal region, others are just locale and third are locale + different region. Because of this,
    We need logic to determine the correct directory name which we should access
     */
    private static String loadCldrDirectoryLocale(String locale) {
        if (!standardLocalesToCldrNamingLocales.containsKey(locale)) {
            String[] components = locale.split("-", 2);
            String language = components[0];
            String region = components[1];

            if (language.equalsIgnoreCase(region)) {
                return language;
            }
        }
        return standardLocalesToCldrNamingLocales.getOrDefault(locale, locale);
    }

    private static void downloadAndGenerateCldrJsonResources(String cldrVersion, List<String> locales) throws IOException {
        String unzippedCldrDir = System.getProperty("java.io.tmpdir") + "/cldr-data-" + cldrVersion;
        unzipFolder(downloadFileToTempDirectory(cldrVersion), Path.of(unzippedCldrDir));

        generateCalendarDatesJson(unzippedCldrDir, locales);
        generateDateFields(unzippedCldrDir, locales);
        generateTimezoneJson(unzippedCldrDir, locales);
    }

    private static String createJsonResourceDirectory(String locale) throws IOException {
        String[] components = locale.split("-", 2);
        String language = components[0];
        String region = components[1];

        String dirPath = "library/src/main/resources/cldr/" + language + "/" + region + "/";
        Files.createDirectories(Paths.get(dirPath));

        return dirPath;
    }

    private static void unzipFolder(File source, Path target) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {

                if (zipEntry.getName().contains("LICENSE")) {
                    zipEntry = zis.getNextEntry();
                }

                boolean isDirectory = zipEntry.getName().endsWith(File.separator);

                Path newPath = zipSlipProtect(zipEntry, target);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }

                    // copy files, nio
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }

    // protect zip slip attack
    private static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir) throws IOException {
        Path targetDirResolved = targetDir.resolve(zipEntry.getName());
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }

    private static File downloadFileToTempDirectory(String cldrVersion) {
        try {
            URL url = new URL(getDownloadCldrUrl(cldrVersion));
            String tempDir = System.getProperty("java.io.tmpdir");
            File file = new File(tempDir + FilenameUtils.getName(url.getPath()));
            FileUtils.copyURLToFile(url, file);
            return file;
        } catch (IOException e) {
            return null;
        }
    }

    private static String getDownloadCldrUrl(String cldrVersion) {
        return "https://github.com/unicode-org/cldr-json/releases/download/"
                + cldrVersion + "/cldr-" + cldrVersion + "-json-full.zip";
    }
}
