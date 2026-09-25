package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {

		goTo("http://localhost:8080/lms/");

		assertEquals("ログイン | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		});

	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {

		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA01");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA02");

		webDriver.findElement(By.className("btn-primary")).click();

		visibilityTimeout(By.id("wrap"), 5);

		assertEquals("コース詳細 | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		});

	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {

		webDriver.findElement(By.className("dropdown")).click();

		visibilityTimeout(By.linkText("ヘルプ"), 5);

		webDriver.findElement(By.linkText("ヘルプ")).click();

		visibilityTimeout(By.id("wrap"), 5);

		assertEquals("ヘルプ | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {

		webDriver.findElement(By.linkText("よくある質問")).click();

		//ハンドルを新しいタブに接続する
		for (String windowHandle : webDriver.getWindowHandles()) {
			if (!webDriver.getWindowHandle().contentEquals(windowHandle)) {
				webDriver.switchTo().window(windowHandle);
				break;
			}
		}

		visibilityTimeout(By.id("wrap"), 5);

		assertEquals("よくある質問 | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		});

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {

		String keyword = "ダウンロード";

		webDriver.findElement(By.id("form")).sendKeys(keyword);
		webDriver.findElement(By.cssSelector("[value=検索]")).click();

		visibilityTimeout(By.id("DataTables_Table_0"), 5);

		//テーブルの行要素をすべて取得
		List<WebElement> rows = webDriver.findElement(By.id("DataTables_Table_0")).findElement(By.cssSelector("tbody"))
				.findElements(By.tagName("tr"));

		for (WebElement row : rows) {

			//検索結果内容を取得
			WebElement titlecell = row.findElement(By.cssSelector("td dl dt > span:nth-of-type(2)"));
			WebElement textcell = row.findElement(By.cssSelector("td dl dd > span:nth-of-type(2)"));

			assertTrue(titlecell.getText().contains(keyword) || textcell.getAttribute("textContent").contains(keyword));

			//内容を表示
			titlecell.click();
		}

		//エビデンス画像のために位置を調整
		scrollBy("150");
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {

		webDriver.findElement(By.id("form")).sendKeys("テスト用キーワード");
		webDriver.findElement(By.cssSelector("[value=クリア]")).click();

		assertEquals("", webDriver.findElement(By.id("form")).getAttribute("value"));

		getEvidence(new Object() {
		});
	}

}
