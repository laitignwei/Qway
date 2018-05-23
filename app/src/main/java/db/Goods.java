package db;

public class Goods {
    private int id;
    private String goods_name;
    private String code;
    private int stock;
    private int current_stock;


    public Goods() {
       super();
    }

    public int optId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String optGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String optCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int optStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int optCurrent_stock() {
        return current_stock;
    }

    public void setCurrent_stock(int current_stock) {
        this.current_stock = current_stock;
    }


}