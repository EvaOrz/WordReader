package com.sunvote.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sunvote.myapplication.R;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Bookmark;
import org.apache.poi.hwpf.usermodel.Bookmarks;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {
    //使用HWPFDocument读文件

    private final String PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/word_test/test.doc";
    private static final String TAG = "MainActivity";
    private TextView contentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);


        contentTv = findViewById(R.id.content);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testReadByDoc();
            }
        });
        findViewById(R.id.web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HtmlActivity.class));
            }
        });
    }

    private void log(Object o) {
        Log.d(TAG, String.valueOf(o));
    }

    public void testReadByDoc() {
        try {
            InputStream is = new FileInputStream(PATH);
            HWPFDocument doc = new HWPFDocument(is);
            //输出书签信息
            this.printInfo(doc.getBookmarks());
            //输出文本
            log(doc.getDocumentText());
            contentTv.setText(doc.getDocumentText());
            Range range = doc.getRange();
            //读整体
            this.printInfo(range);
            //读表格
            this.readTable(range);
            //读列表
            this.readList(range);
            this.closeStream(is);
        } catch (Exception e) {

        }
    }

    /**
     * 关闭输入流
     *
     * @param is
     */
    private void closeStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 输出书签信息
     *
     * @param bookmarks
     */
    private void printInfo(Bookmarks bookmarks) {
        int count = bookmarks.getBookmarksCount();
        log("书签数量：" + count);
        Bookmark bookmark;
        for (int i = 0; i < count; i++) {
            bookmark = bookmarks.getBookmark(i);
            log("书签" + (i + 1) + "的名称是：" + bookmark.getName());
            log("开始位置：" + bookmark.getStart());
            log("结束位置：" + bookmark.getEnd());
        }
    }

    /**
     * 读表格
     * 每一个回车符代表一个段落，所以对于表格而言，每一个单元格至少包含一个段落，每行结束都是一个段落。
     *
     * @param range
     */
    private void readTable(Range range) {
        //遍历range范围内的table。
        TableIterator tableIter = new TableIterator(range);
        Table table;
        TableRow row;
        TableCell cell;
        while (tableIter.hasNext()) {
            table = tableIter.next();
            int rowNum = table.numRows();
            for (int j = 0; j < rowNum; j++) {
                row = table.getRow(j);
                int cellNum = row.numCells();
                for (int k = 0; k < cellNum; k++) {
                    cell = row.getCell(k);
                    //输出单元格的文本
                    log(cell.text().trim());
                }
            }
        }
    }

    /**
     * 读列表
     *
     * @param range
     */
    private void readList(Range range) {
        int num = range.numParagraphs();
        Paragraph para;
        for (int i = 0; i < num; i++) {
            para = range.getParagraph(i);
            if (para.isInList()) {
                log("list: " + para.text());
            }
        }
    }

    /**
     * 输出Range
     *
     * @param range
     */
    private void printInfo(Range range) {
        //获取段落数
        int paraNum = range.numParagraphs();
        log(paraNum);
        for (int i = 0; i < paraNum; i++) {
            log("段落" + (i + 1) + "：" + range.getParagraph(i).text());
            if (i == (paraNum - 1)) {
                this.insertInfo(range.getParagraph(i));
            }
        }
        int secNum = range.numSections();
        log(secNum);
        Section section;
        for (int i = 0; i < secNum; i++) {
            section = range.getSection(i);
            log(section.getMarginLeft());
            log(section.getMarginRight());
            log(section.getMarginTop());
            log(section.getMarginBottom());
            log(section.getPageHeight());
            log(section.text());
        }
    }

    /**
     * 插入内容到Range，这里只会写到内存中
     *
     * @param range
     */
    private void insertInfo(Range range) {
        range.insertAfter("Hello");
    }
}
