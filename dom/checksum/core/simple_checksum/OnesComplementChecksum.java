package checksum.core.simple_checksum;

import static checksum.policy.ChecksumData.DIFFERENT_DATA;
import static checksum.policy.ChecksumData.ORIGIN_DATA;

/**
 * 단순 가산 체크썸 (검사합 체크썸이라고도 함)
 * : 1의 보수를 이용한 간단한 체크썸
 * 단점 : 데이터 워드(메시지)의 블록(바이트) 순서에 민감하지 않다는 것입니다.
 */
public class OnesComplementChecksum {

    public static void main(String[] args) {
        //원본 데이터
        String receivedData = ORIGIN_DATA;
        int checksum = getChecksum(receivedData);

        //비교 데이터
        String differentData = DIFFERENT_DATA;
        String sameData = ORIGIN_DATA;

        // 테스트
        boolean resultOfDifferentData = verifyCheckSum(differentData, checksum);
        boolean resultOfSameData = verifyCheckSum(sameData, checksum);

        //결과
        System.out.printf("received checksum : 0x%08X\n", checksum);
        System.out.println("differentData와 checkSum이 같은가? : " + resultOfDifferentData);
        System.out.println("sameData와 checkSum이 같은가? : " + resultOfSameData);
    }

    /**
     * 체크썸 구하는 메서드
     */
    public static int getChecksum(String data) {
        byte[] byteData = data.getBytes();
        // ~sum : 비트 반전  ex) 1101 -> 0010
        return ~sumAllByteData(byteData);
    }

    /**
     * 데이터의 모든 바이트들을 16비트로 나누고 모두 더하는 메서드
     */
    private static int sumAllByteData(byte[] byteData) {
        int sum = 0;
        for (int i = 0; i < byteData.length; i += 2) {
            int word = 0;
            // 자바의 바이트 (-128 ~ 127), & 0xFF = 부호 없는 값(0 ~ 255)**으로 변환
            word += (byteData[i] & 0xFF) << 8;
            // 마지막 바이트가 아니라면 sum에 더함.
            if (i + 1 < byteData.length) {
                word += (byteData[i + 1] & 0xFF);
            }
            sum += word;
            sum = extract16bits(sum);
        }
        return sum;
    }

    /**
     * 체크썸 검증 메서드
     * 결과가 0xFFFF (모든 비트가 1)이라면 true or false
     */
    public static boolean verifyCheckSum(String data, int receivedChecksum) {
        int sum = (sumAllByteData(data.getBytes()) + receivedChecksum) & 0xFFFF;
        sum = extract16bits(sum) & 0xFFFF;
        return sum == 0xFFFF;
    }

    /**
     * 16진수가 넘을 시, 16진수로 만드는 메서드
     * sum & 0xFFFF :  17비트 생기면 16비트만 남김, 17비트 버림
     * sum >> 16 : 17비트 오른쪽으로 16비트 밀어서 17번째 캐리니블 만 남김
     */
    private static int extract16bits(int sum) {
        while ((sum >> 16) > 0) {
            sum = (sum & 0xFFFF) + (sum >> 16);
        }
        return sum;
    }
}