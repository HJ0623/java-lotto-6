package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.util.*;

public class Application {
    public static final int minInputMoney = 1000; // 최소 inputmoney값 설정해주기
    int inputMoney = 0;
    int totalPrize = 0;
    List<Integer> inputWinningNumber = new ArrayList<>(); // ArrayList로 초기화
    int inputBonusNumber = 0;      //try를 사용해서 변수를 위에다가 다 선언
    List<List<Integer>> purchasedLotto = new ArrayList<>(); //로또를 여러개 사야하니깐

    public static void main(String[] args) {
        Application app = new Application();
        app.run();

    }

    public void run() {
        getInputMoney();
        generateLotto();
        getInputWinningNumber();
        getInputBonusNumber();
        comparedLottoNumber();

    }

    public void getInputMoney() {
        try {
            System.out.println("구입금액을 입력해 주세요.");
            inputMoney = Integer.parseInt(Console.readLine());
            if (inputMoney < minInputMoney) {
                throw new IllegalArgumentException("[ERROR] 구입금액은 1000원이상이여야합니다");
            } else if (inputMoney % minInputMoney != 0) {
                throw new IllegalArgumentException("[ERROR] 구입금액은 1000원 단위여야합니다");
            }

        } catch (NumberFormatException e) {
            System.out.println("[ERROR] 잘못된 입력입니다 숫자를 입력해주세요");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void generateLotto() {
        // 로또 생성
        int numberOfLotto = inputMoney / minInputMoney;
        System.out.println(numberOfLotto + "개를 구매하였습니다.");
        for (int i = 0; i < numberOfLotto; i++) {
            List<Integer> lottoNumbers = Randoms.pickUniqueNumbersInRange(1, 45, 6);
            purchasedLotto.add(lottoNumbers);
            List<Integer> sortedNumbers = lottoNumbers.stream()
                    .sorted()
                    .toList();
            System.out.println(sortedNumbers);
        }
    }

    public void getInputWinningNumber() {
        try {
            System.out.println("당첨 번호를 입력해주세요");
            String input = Console.readLine();
            inputWinningNumber = Arrays.stream(input.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            if (inputWinningNumber.size() != 6) {
                throw new IllegalArgumentException("[ERROR] 로또 번호는 6개를 입력해야합니다");
            }
            if (inputWinningNumber.size() != new HashSet<>(inputWinningNumber).size()) {
                throw new IllegalArgumentException("[ERROR] 당첨 번호는 중복되지 않는 숫자여야 합니다.");
            }
            for (int winningNumber : inputWinningNumber) {
                if (!(1 <= winningNumber && winningNumber <= 45)) {
                    throw new IllegalArgumentException("[ERROR] 당첨번호는 1~45사이의 숫자입니다");
                }
            }
        }
        catch (NumberFormatException e) {
            System.out.println("[ERROR] 잘못된 입력입니다 숫자를 입력해주세요");
            System.exit(1);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public void getInputBonusNumber() {
        try {
            System.out.println("보너스 번호를 입력해주세요");
            inputBonusNumber = Integer.parseInt(Console.readLine());
            if (!(1 <= inputBonusNumber && inputBonusNumber <= 45)) {
                throw new IllegalArgumentException("[ERROR] 보너스번호는 1~45사이의 숫자입니다");
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] 잘못된 입력입니다 숫자를 입력해주세요");
            System.exit(1);
        } catch (
                IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void comparedLottoNumber() {
        Map<Integer, Integer> prizeCountMap = new HashMap<>();
        prizeCountMap.put(1, 0);
        prizeCountMap.put(2, 0);
        prizeCountMap.put(3, 0);
        prizeCountMap.put(4, 0);
        prizeCountMap.put(5, 0);

        //비교
        for (List<Integer> lotto : purchasedLotto) {   //체크를 할때 내가 산 로또 갯수만큼 반복하고 -> 6개의 산 로또번호를 판별하는거
            int matchCount = 0;
            boolean isBonusNumberMatched = false;


            for (int num : lotto) {
                if (inputWinningNumber.contains(num)) {
                    matchCount++;
                }
            }
            if (lotto.contains(inputBonusNumber)) {
                isBonusNumberMatched = true;
            }
            if (matchCount == 6) {
                prizeCountMap.put(1, prizeCountMap.get(1) + 1);
                totalPrize += 2000000000;
            } else if (matchCount == 5 && isBonusNumberMatched) {
                prizeCountMap.put(2, prizeCountMap.get(2) + 1);
                totalPrize += 30000000;
            } else if (matchCount == 5 ) {
                prizeCountMap.put(3, prizeCountMap.get(3) + 1);
                totalPrize += 1500000;
            } else if (matchCount == 4) {
                prizeCountMap.put(4, prizeCountMap.get(4) + 1);
                totalPrize += 50000;
            } else if (matchCount == 3) {
                prizeCountMap.put(5, prizeCountMap.get(5) + 1);
                totalPrize += 5000;
            }
        }


        System.out.println("당첨 통계");
        System.out.println("---");
        System.out.println("3개 일치 (5,000원) - " + prizeCountMap.get(5) + "개");
        System.out.println("4개 일치 (5,0000원) - " + prizeCountMap.get(4) + "개");
        System.out.println("5개 일치 (1,500,000원) - " + prizeCountMap.get(3) + "개");
        System.out.println("5개와 보너스 번호 일치 (30,000,000원) - " + prizeCountMap.get(2) + "개");
        System.out.println("6개 일치 (2,000,000,000원) - " + prizeCountMap.get(1) + "개");

        double profitRate = ((double) totalPrize / inputMoney) * 100;
        String resultProfit = String.format("%.2f", profitRate);
        System.out.println("총 수익률은 " + resultProfit + "%입니다.");
        }


}

