package com.example.ArtAuction_24.domain.bid.entity;

import com.example.ArtAuction_24.domain.product.entity.Product;
import java.util.List;

public class KeyValues {
    private final Product product;
    private final List<Bid> bids;

    public KeyValues(Product product, List<Bid> bids) {
        this.product = product;
        this.bids = bids;
    }

    public Product getProduct() {
        return product;
    }

    public List<Bid> getBids() {
        return bids;
    }
}
