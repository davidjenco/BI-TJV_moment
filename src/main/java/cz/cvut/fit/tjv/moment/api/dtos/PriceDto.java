package cz.cvut.fit.tjv.moment.api.dtos;

public class PriceDto {
    public int price;

    public PriceDto() {
    }

    public PriceDto(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
