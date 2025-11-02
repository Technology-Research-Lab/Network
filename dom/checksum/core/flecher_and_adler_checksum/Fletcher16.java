package checksum.core.flecher_and_adler_checksum;

import static checksum.policy.ChecksumData.ORIGIN_DATA;

/**
 * 플래처 16 체크썸
 * 역사 : 플레처 체크섬 은 1970년대 후반 로렌스 리버모어 연구소 의 존 G. 플레처(1934~2012)가 고안한 위치 종속 체크섬을 계산하기 위한 알고리즘
 * 장점 : 가볍고 빠름
 * 단점 : 충돌 가능성 존재
 */
public class Fletcher16 {

    private static final int MOD_FLETCHER = 255;

    public static void main(String[] args) {
        String orgData = ORIGIN_DATA;
        byte[] data = orgData.getBytes();

        int checksum = computeFletcher16(data);
        System.out.printf("Fletcher-16 체크섬: 0x%04X\n", checksum);
    }

    public static int computeFletcher16(byte[] data) {
        int sum1 = 0;
        int sum2 = 0;

        for (byte b : data) {
            sum1 = (sum1 + (b & 0xFF)) % MOD_FLETCHER; // 바이트를 0~255로 변환
            sum2 = (sum2 + sum1) % MOD_FLETCHER;
        }

        return (sum2 << 8) | sum1; // 16비트 체크섬
    }
}

