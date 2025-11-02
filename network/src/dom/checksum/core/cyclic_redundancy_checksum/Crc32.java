package dom.checksum.core.cyclic_redundancy_checksum;

import static dom.checksum.policy.ChecksumData.DIFFERENT_DATA;
import static dom.checksum.policy.ChecksumData.ORIGIN_DATA;

/**
 * 순환 중복 검사 체크썸
 * 역사 : CRC의 기본 아이디어는 1960년대 초에 개발되었습니다.
 * 데이터 전송 오류를 효과적으로 감지하기 위해 고안된 기술입니다. 하드디스크, SSD 등 저장 장치에서 저장된 데이터의 손상 여부를 검사하는 데 활용
 * 장점 : 데이터 전송 오류를 거의 확실하게 잡음, 버스트 오류(burst error) 잘 잡음.
 * 단점 : 느림, 구현 복잡, 출력길이 제한전
 */
public class Crc32 {

    private static final int POLYNOMIAL = 0x04C11DB7;
    private static final int INITIAL_VALUE = 0xFFFFFFFF;
    private static final int XOR_OUT = 0xFFFFFFFF;
    private static final int[] CRC_TABLE = new int[256];

    // CRC 룩업 테이블 초기화 (중복 검증 계산을 회피하기 위해 테이블을 이용)
    static {
        for (int i = 0; i < 256; i++) {
            int crc = i;
            for (int j = 0; j < 8; j++) {
                if ((crc & 1) == 1) {
                    crc = (crc >>> 1) ^ POLYNOMIAL;
                } else {
                    crc = (crc >>> 1);
                }
            }
            CRC_TABLE[i] = crc;
        }
    }

    public static void main(String[] args) {
        //원본 데이터
        String orgData = ORIGIN_DATA;
        byte[] data = orgData.getBytes();
        int crc = calculateCrc32(data);

        //비교 데이터
        String diffData = DIFFERENT_DATA;
        byte[] modifiedData = diffData.getBytes();
        int diffCrc = calculateCrc32(modifiedData);

        //테스트
        System.out.printf("orgData CRC-32: 0x%08X\n", crc);
        System.out.println("orgData와 checkSum이 같은가?: " + verifyCrc32(data, crc));
        System.out.println("diffData와 checkSum이 같은가?: " + verifyCrc32(data, diffCrc));
    }

    /**
     * 바이트 배열에 대한 CRC-32 체크썸을 계산
     * 데이터와 최종 나머지를 반전시키는 (Reflect Data / Reflect Remainder) 표준 방식 사용
     */
    public static int calculateCrc32(byte[] data) {
        int crc = INITIAL_VALUE;
        if (data == null || data.length == 0) {
            return crc ^ XOR_OUT;
        }
        for (byte b : data) {
            crc = (crc >>> 8) ^ CRC_TABLE[(crc ^ b) & 0xFF];
        }
        return crc ^ XOR_OUT;
    }

    /**
     * 체크썸 검증.
     */
    public static boolean verifyCrc32(byte[] data, int receivedCrc) {
        int calculatedCrc = calculateCrc32(data);
        return calculatedCrc == receivedCrc;
    }
}