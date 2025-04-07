package com.sprint.mission.discodeit.util;


public class BinaryContentUtil {
  private BinaryContentUtil() {
  }
//  public static byte[] convertToBytes(MultipartFile mFile) {
//    try {
//      return mFile.getBytes();
//    } catch (IOException e) {
//      throw new CustomException(ErrorCode.DEFAULT_ERROR_MESSAGE);
//    }
//  }
//
//  public static String convertToBase64(BinaryContent binaryContent) {
//    if (binaryContent == null || binaryContent.getBytes() == null) {
//      return null;
//    }
//    return Base64.getEncoder().encodeToString(binaryContent.getBytes());
//  }
//
//  public static List<String> convertMultipleBinaryContentToBase64(List<BinaryContent> contents) {
//    if (contents == null || contents.isEmpty()) {
//      return Collections.emptyList();
//    }
//
//    return contents.stream()
//        .map(BinaryContentUtil::convertToBase64)
//        .toList();
//  }

}
