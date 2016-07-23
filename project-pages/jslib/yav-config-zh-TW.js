/***********************************************************************
 * YAV - Yet Another Validator  v1.4.0                                 *
 * Copyright (C) 2005-2006-2007                                        *
 * Author: Federico Crivellaro <f.crivellaro@gmail.com>                *
 * WWW: http://yav.sourceforge.net                                     *
 ***********************************************************************/

// CHANGE THESE VARIABLES FOR YOUR OWN SETUP 

// if you want yav to highligh fields with errors 
inputhighlight = true; 
// if you want to use multiple class names
multipleclassname=true;
// classname you want for the error highlighting 
inputclasserror = 'inputError'; 
// classname you want for your fields without highlighting 
inputclassnormal = 'inputNormal'; 
// classname you want for the inner html highlighting 
innererror = 'innerError'; 
// div name where errors will appear (or where jsVar variable is dinamically defined) 
errorsdiv = 'errorsDiv'; 
// if you want yav to alert you for javascript errors (only for developers) 
debugmode = false; 
// if you want yav to trim the strings 
trimenabled = true; 

// change these to set your own decimal separator and your date format 
DECIMAL_SEP ='.'; 
THOUSAND_SEP = ','; 
DATE_FORMAT = 'yyyy-MM-dd'; 

// change this to set your own rule separator
RULE_SEP = '|';

// Translate By Cloudream (cloudream@gmail.com) 

// change these strings for your own translation (do not change {n} values!) 
HEADER_MSG = '數據驗證未通過:'; 
FOOTER_MSG = '請重試.'; 
DEFAULT_MSG = '數據不正確.'; 
REQUIRED_MSG = '請輸入 {1} .'; 
ALPHABETIC_MSG = '{1} 格式錯誤. 允許的字符: A-Za-z'; 
ALPHANUMERIC_MSG = '{1} 格式錯誤. 允許的字符: A-Za-z0-9'; 
ALNUMHYPHEN_MSG = '{1} 格式錯誤. 允許的字符: A-Za-z0-9\-_'; 
ALNUMHYPHENAT_MSG = '{1} 格式錯誤. 允許的字符: A-Za-z0-9\-_@'; 
ALPHASPACE_MSG = '{1} 格式錯誤. 允許的字符: A-Za-z0-9\-_"空格"'; 
MINLENGTH_MSG = '{1} 需至少 {2} 個字符.'; 
MAXLENGTH_MSG = '{1} 需少於 {2} 個字符.'; 
NUMRANGE_MSG = '{1} 需為 {2} 間的數字.'; 
DATE_MSG = '{1} 日期錯誤,格式: ' + DATE_FORMAT + '.'; 
NUMERIC_MSG = '{1} 需為數字.'; 
INTEGER_MSG = '{1} 需為證書'; 
DOUBLE_MSG = '{1} 需為小數.'; 
REGEXP_MSG = '{1} 格式錯誤. 允許的格式: {2} .'; 
EQUAL_MSG = '{1} 需等於 {2} .'; 
NOTEQUAL_MSG = '{1} 需不同於 {2} .'; 
DATE_LT_MSG = '{1} 需早於 {2} .'; 
DATE_LE_MSG = '{1} 需早於或等於 {2} .'; 
EMAIL_MSG = '{1} 需為有效的E-mail地址.'; 
EMPTY_MSG = '{1} 需為空.';