package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application {


    public static void main(String[] args) {
        int inputMoney = 0;
        int totalPrize = 0;
        int firstCount = 0;
        int secondCount = 0;
        int thirdCount = 0;
        int fourthCount = 0;
        int fifthCount = 0;
        List<Integer> inputWinningNumber = new ArrayList<>(); // ArrayList로 초기화
        int inputBonusNumber=0;      //try를 사용해서 변수를 위에다가 다 선언

        {
            try {
                System.out.println("구입금액을 입력해 주세요.");
                inputMoney = Integer.parseInt(Console.readLine());
                if (inputMoney < 1000) {
                    throw new IllegalArgumentException("[ERROR] 구입금액은 1000원이상이여야합니다");
                } else if (inputMoney % 1000 != 0) {
                    throw new IllegalArgumentException("[ERROR] 구입금액은 1000원 단위여야합니다");
                }

            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 잘못된 입력입니다 숫자를 입력해주세요");
                System.exit(1);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }

            // 로또 생성
            int numberOfLotto = inputMoney / 1000;
            List<List<Integer>> purchasedLotto = new ArrayList<>(); //로또를 여러개 사야하니깐
            System.out.println(numberOfLotto + "개를 구매하였습니다.");
            for (int i = 0; i < numberOfLotto; i++) {
                List<Integer> lottoNumbers = Randoms.pickUniqueNumbersInRange(1, 45, 6);
                purchasedLotto.add(lottoNumbers);
                List<Integer> sortedNumbers = lottoNumbers.stream()
                        .sorted()
                        .toList();
                System.out.println(sortedNumbers);
            }

            //값 입력
            try {
                System.out.println("당첨 번호를 입력해주세요");
                String input = Console.readLine();
                inputWinningNumber = Arrays.stream(input.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList();
                if(inputWinningNumber.size() != 6) {
                    throw  new IllegalArgumentException("[ERROR] 로또 번호는 6개를 입력해야합니다");
                    }
                for (int winningNumber : inputWinningNumber) {
                    if (!(1 <= winningNumber && winningNumber <= 45)) {
                        throw new IllegalArgumentException("[ERROR] 당첨번호는 1~45사이의 숫자입니다");
                    }
                }
                System.out.println("보너스 번호를 입력해주세요");
                inputBonusNumber = Integer.parseInt(Console.readLine());
                if (!(1 <= inputBonusNumber && inputBonusNumber <= 45)) {
                    throw new IllegalArgumentException("[ERROR] 보너스번호는 1~45사이의 숫자입니다");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 잘못된 입력입니다 숫자를 입력해주세요");
                System.exit(1);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }



            //비교
            for (List<Integer> lotto : purchasedLotto) {   //체크를 할때 내가 산 로또 갯수만큼 반복하고 -> 6개의 산 로또번호를 판별하는거
                int matchCount = 0;
                boolean bonusMatchCount = false;

                for (int num : lotto) {
                    if (inputWinningNumber.contains(num)) {
                        matchCount++;
                    }
                }
                if (lotto.contains(inputBonusNumber)) {
                    bonusMatchCount = true;
                }
                if (matchCount == 6) {
                    firstCount++;
                    totalPrize += 2000000000;
                } else if (matchCount == 5 && bonusMatchCount) {
                    secondCount++;
                    totalPrize += 30000000;
                } else if (matchCount == 5 && !bonusMatchCount) {
                    thirdCount++;
                    totalPrize += 1500000;
                } else if (matchCount == 4) {
                    fourthCount++;
                    totalPrize += 50000;
                } else if (matchCount == 3) {
                    fifthCount++;
                    totalPrize += 5000;
                }
            }

            //결과 출력
            System.out.print("당첨 통계");
            System.out.println("---");
            System.out.println("3개 일치 (5,000원) - " + fifthCount + "개");
            System.out.println("4개 일치 (5,0000원) - " + fourthCount + "개");
            System.out.println("5개 일치 (1,500,000원) - " + thirdCount + "개");
            System.out.println("5개와 보너스 번호 일치 (30,000,000원) - " + secondCount + "개");
            System.out.println("6개 일치 (2,000,000,000원) - " + firstCount + "개");
            double profitRate = ((double) totalPrize / inputMoney) * 100;
            System.out.printf("총 수익률은 %.2f%%입니다.", profitRate);
        }
    }
}