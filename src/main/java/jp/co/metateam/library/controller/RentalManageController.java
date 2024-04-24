package jp.co.metateam.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import jakarta.validation.Valid;

import jp.co.metateam.library.service.AccountService;
import jp.co.metateam.library.service.RentalManageService;
import jp.co.metateam.library.service.StockService;
import lombok.extern.log4j.Log4j2;

import jp.co.metateam,library.model.RentalManage;

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
        List<RentalManage> rentalmanageaList = this.rentalManageService.findAll();

        // 貸出一覧画面に渡すデータをmodelに追加
        model.addAttribute("rentalmanageaList",rentalmanageaList);
        
        // 貸出一覧画面に遷移
        return "rental/index";
    }

    //登録処理
    @GetMapping("/rental/add")
    public String add(Model model){
        if (!model.containsAttribute("rentalmanageDto")){
            model.addAttribute("rentalmanageDto",new RentalManageDto());
        } //もしDtoに変更があったとき、ページ更新時に反映するため

        return "rental/add";

    }

    @PostMapping("/rental/add")
    public String register(@vaild @ModelAttribute RentalMnageDto rentalmanageDto, BindingResult result, RedirectAttributes ra){
        try {

            if(result.hasErrors()){
                throw new Exception("Validation error.");
            }
        
        this.rentalManageService.save(rentalmanageDto);

        return"redirect:/rental/add";
        }catch (Exception e) {
            log.error(e.getMessage());

            ra.addFlashAttribute("rentalmanageDto", rentalmanageDto);
            //test

            return "redirect:/rental/add";
        }
    }
}
