package checksum.core.flecher_and_adler_checksum;

import static checksum.policy.ChecksumData.ORIGIN_DATA;

/**
 * 애들러 32비트 체크썸
 * 역사 : Adler-32 는 1995년 Mark Adler 가 Fletcher의 체크섬을 수정하여 작성한 체크섬 알고리즘
 * 장점 : Crc 보다는 신뢰 하락, 하지만 가볍고 빠름.
 */
public class Adler32Checksum {
    //2n16 보다 작고 소수 (충돌 줄임)
    private static final int MOD_ADLER = 65521;

    public static void main(String[] args) {
        String orgData = ORIGIN_DATA;
        byte[] data = orgData.getBytes();

        int checksum = computeAdler32(data);
        //0x%08X : 32비트 0으로 채워서 표기법 println은 십진수로 알아서 변환하기 때문
        System.out.printf("Adler-32 체크섬: 0x%08X\n", checksum);
    }

    // 애들러 방법으로 체크썸 구하는 로직
    public static int computeAdler32(byte[] data) {
        int a = 1;
        int b = 0;

        for (byte d : data) {
            a = (a + (d & 0xFF)) % MOD_ADLER;
            b = (b + a) % MOD_ADLER;
        }

        return (b << 16) | a;
    }

}

