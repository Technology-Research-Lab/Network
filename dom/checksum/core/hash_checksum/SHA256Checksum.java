package checksum.core.hash_checksum;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static checksum.policy.ChecksumData.DIFFERENT_DATA;
import static checksum.policy.ChecksumData.ORIGIN_DATA;

/**
 * 해시 체크썸
 * : 해시 알고리즘 SHA-256을 이용한 간단한 체크섬.
 */
public class SHA256Checksum {

    public static void main(String[] args) {
        try {
            //원본 데이터
            String org = ORIGIN_DATA;
            byte[] receivedChecksum = computeSHA256(org.getBytes());

            //비교 데이터
            byte[] orgChecksum = computeSHA256(ORIGIN_DATA.getBytes());
            byte[] diffChecksum = computeSHA256(DIFFERENT_DATA.getBytes());

            // 테스트
            System.out.println(("receivedChecksum: " + bytesToHex(receivedChecksum)));
            System.out.println(("orgChecksum 검증 결과: " + Arrays.equals(receivedChecksum, orgChecksum)));
            System.out.println(("diffChecksum 검증 결과: " + Arrays.equals(receivedChecksum, diffChecksum)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // 바이트 배열의 SHA-256 체크섬 계산 -> MessageDigest : 자바에서 구현된 안정된 sha-256 알고리즘 계산 인터페이스
    public static byte[] computeSHA256(byte[] data) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-256").digest(data);
    }

    // 바이트 배열을 16진수 문자열로 변환
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

