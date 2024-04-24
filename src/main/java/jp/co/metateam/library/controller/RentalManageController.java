package jp.co.metateam.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
//import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.RentalManage;
import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.model.Stock;

//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import jakarta.validation.Valid;

import jp.co.metateam.library.service.AccountService;
import jp.co.metateam.library.service.RentalManageService;
import jp.co.metateam.library.service.StockService;
import jp.co.metateam.library.values.RentalStatus;
import lombok.extern.log4j.Log4j2;


/**
 * 貸出管理関連クラスß
 */
@Log4j2
@Controller
public class RentalManageController {

    private final AccountService accountService;
    private final RentalManageService rentalManageService;
    private final StockService stockService;

    @Autowired
    public RentalManageController(
        AccountService accountService, 
        RentalManageService rentalManageService, 
        StockService stockService
    ) {
        this.accountService = accountService;
        this.rentalManageService = rentalManageService;
        this.stockService = stockService;
    }

    /**
     * 貸出一覧画面初期表示
     * @param model
     * @return
     */
    @GetMapping("/rental/index")
    public String index(Model model) {
        // 貸出管理テーブルから全件取得
        List<RentalManage> rentalManageList = this.rentalManageService.findAll();

        // 貸出一覧画面に渡すデータをmodelに追加
        model.addAttribute("rentalManageList",rentalManageList);
        
        // 貸出一覧画面に遷移
        return "rental/index";
    }

    //登録処理
    @GetMapping("/rental/add")
    public String add(Model model){
        List<Account> accountList = accountService.findAll();
        List<Stock> stockList = stockService.findAll(); //findStockAvailableAllで在庫として利用可能で削除されていないものをとってくる

        if (!model.containsAttribute("rentalManageDto")){
            model.addAttribute("rentalManageDto",new RentalManageDto());
        } //もしDtoに変更があったとき、ページ更新時に反映するため
          //エラーがあって登録画面に戻る際、入力内容が入った状態で返せるように

        model.addAttribute("accounts", accountList);
        model.addAttribute("stockList", stockList);
        model.addAttribute("rentalStatus", RentalStatus.values());
        
        return "rental/add";

    }


    @PostMapping("/rental/add")
    public String register(@Valid @ModelAttribute RentalManageDto rentalManageDto, BindingResult result, RedirectAttributes ra){
        try {

            if(result.hasErrors()){
                throw new Exception("Validation error.");
            }
        
        this.rentalManageService.save(rentalManageDto);

        return"redirect:/rental/index"; //redirectがないと一覧の画面にデータがないまま戻る
        }catch (Exception e) {
            log.error(e.getMessage());

            ra.addFlashAttribute("rentalManageDto", rentalManageDto);
            //test

            return "redirect:/rental/add";
        }
    }
}
