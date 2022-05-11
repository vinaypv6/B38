package generic;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;




public class Utility {

	public static String getProperty(String path,String key) {
		String v="";
		try 
		{
			Properties p=new Properties();
			p.load(new FileInputStream(path));
			v=p.getProperty(key);
		}
		catch (Exception e) {
			
		}
		return v;
	}
	
	public static String getXLData(String path,String sheet,int row,int col) {
		String v="";
		try 
		{
			Workbook wb = WorkbookFactory.create(new FileInputStream(path));
			v=wb.getSheet(sheet).getRow(row).getCell(col).toString();
			wb.close();
		}
		catch (Exception e)
		{
		}
		return v;
	}
	
	
	public static int getXlRowCount(String path,String sheet) {
		int row=0;
		try 
		{
			Workbook wb = WorkbookFactory.create(new FileInputStream(path));
			row=wb.getSheet(sheet).getLastRowNum();
			wb.close();
		}
		catch (Exception e)
		{
		}
		return row;
	}
	public static int getXlColCount(String path,String sheet,int row) {
		int col=0;
		try 
		{
			Workbook wb = WorkbookFactory.create(new FileInputStream(path));
			col=wb.getSheet(sheet).getRow(row).getLastCellNum();
			wb.close();
		}
		catch (Exception e)
		{
		}
		return col;
	}
	
	public static Iterator<String[]> getDataFromExcel(String path,String sheetName) {
		ArrayList<String[]> data=new ArrayList<String[]>();
		try 
		{
			Workbook wb = WorkbookFactory.create(new FileInputStream(path));
			Sheet sheet = wb.getSheet(sheetName);
			int rc= sheet.getLastRowNum();
			
			for(int i=1;i<=rc;i++) // skip 0 , as it is a header
			{
				try
				{
						int cc=sheet.getRow(i).getLastCellNum();
						String[] cell=new String[cc];
						for(int j=0;j<cc;j++) {
								try 
								{
									String v=sheet.getRow(i).getCell(j).toString();
									cell[j]=v;
								}
								catch (Exception e) 
								{
									cell[j]="";
								}
						}
						data.add(cell);
				}
				catch (Exception e) 
				{
					
				}
				
			
			}
			wb.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return data.iterator();
	}
}
