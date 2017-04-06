package gopetting.assignment.com.cartapp.model;

/**
 * Created by viraj on 4/6/16.
 */

public class Product {
    private String imageId;
    private String title;
    private String description;

    public Product(String imageId, String title, String description) {
        this.imageId = imageId;
        this.title = title;
        this.description = description;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
