package sample.pets;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import sample.gameComponents.Food;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class Pet implements Runnable, Externalizable {

    protected static final int MIN_SATIETY = 1;
    protected static final int MAX_SATIETY = 100;
    protected static final int MIN_AGE = 0;
    protected static final int MAX_AGE = 3;
    protected static final long TRAUR_TIME = 4;

    private String name;
    private IntegerProperty age;
    private IntegerProperty satiety;
    private Date lastUpdate;
    private boolean isAlive;

    private int satietyChangePercent;
    private long timeToBeBornInSeconds;
    private long needChangeTimeInSeconds;
    private Food food;

    private Image age0Image;
    private Image age1Image;
    private Image age2Image;
    private Image age3Image;
    private Image ripImage;

    {
        isAlive=true;
        this.age = new SimpleIntegerProperty(MIN_AGE);
        this.satiety = new SimpleIntegerProperty(MAX_SATIETY);
        this.lastUpdate=new Date();
    }

    @Override
    public void run() {
        Platform.runLater(this::decreaseNeeds);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        if (age<4) {
            if (this.age != null)
                this.age.set(age);
            else
                this.age = new SimpleIntegerProperty(age);
        }
    }

    public void setSatiety(int satiety) {
        this.satiety=new SimpleIntegerProperty(satiety);
    }

    public String getName() {
        return name;
    }

    public IntegerProperty getSatiety() {
        return satiety;
    }

    public long getTimeToBeBornInSeconds(){
        return this.timeToBeBornInSeconds;
    }

    public void setTimeToBeBornInSeconds(long timeToBeBornInSeconds){
        this.timeToBeBornInSeconds = timeToBeBornInSeconds;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getSatietyChangePercent() {
        return satietyChangePercent;
    }

    public void setSatietyChangePercent(int satietyChangePercent) {
        this.satietyChangePercent = satietyChangePercent;
    }

    public long getNeedChangeTimeInSeconds() {
        return needChangeTimeInSeconds;
    }

    public void setNeedChangeTimeInSeconds(long needChangeTimeInSeconds) {
        this.needChangeTimeInSeconds = needChangeTimeInSeconds;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date date) {
        this.lastUpdate=date;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public void changeSatiety(int satiety) {
        this.satiety.setValue(this.satiety.get() + satiety);
        if (this.satiety.greaterThan(MAX_SATIETY).get())
            this.satiety.setValue(MAX_SATIETY);
    }

    public IntegerProperty getAge() {
        return age;
    }

    protected abstract void decreaseNeeds();

    public Image getRipImage() {
        return this.ripImage;
    }

    public Image getImageByAge(){
        switch (this.age.get()){
            case 0: {
                return this.getAge0Image();
            }
            case 1:{
                return this.getAge1Image();
            }
            case 2:{
                return this.getAge2Image();
            }
            case 3:{
                return this.getAge3Image();
            }
            default:{
                return null;
            }
        }
    }

    public Image getAge0Image() {
        return age0Image;
    }

    public void setAge0Image(Image age0Image) {
        this.age0Image = age0Image;
    }

    public Image getAge1Image() {
        return age1Image;
    }

    public Image getAge2Image() {
        return age2Image;
    }

    public void setAge2Image(Image age2Image) {
        this.age2Image = age2Image;
    }

    public Image getAge3Image() {
        return age3Image;
    }

    public void setAge3Image(Image age3Image) {
        this.age3Image = age3Image;
    }

    public void setAge1Image(Image age1Image) {
        this.age1Image = age1Image;
    }

    public void setRipImage(Image ripImage) {
        this.ripImage = ripImage;
    }


    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeBoolean(this.isAlive());
        objectOutput.writeObject(this.getName());
        objectOutput.writeInt(this.getAge().intValue());
        if (this.getAge().get()==0) {
            long timeDiffInMilliSeconds=new Date().getTime() - this.getLastUpdate().getTime();
            long timeDiffInSeconds= TimeUnit.MILLISECONDS.toSeconds(timeDiffInMilliSeconds);
            long timeToBeBornDiff=this.getTimeToBeBornInSeconds()-timeDiffInSeconds;
            objectOutput.writeLong(timeToBeBornDiff);
        }
        if (this.isAlive()) this.setLastUpdate(new Date());
        objectOutput.writeObject(this.getLastUpdate());
        objectOutput.writeInt(this.getSatiety().intValue());
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.setAlive(objectInput.readBoolean());
        this.setName((String)objectInput.readObject());
        this.setAge((objectInput.readInt()));
        if (this.getAge().get()==0) {
            long diff=objectInput.readLong();
            if (diff<=0)diff=1;
            this.setTimeToBeBornInSeconds(diff);
        }
        this.setLastUpdate((Date) objectInput.readObject());

        int satiety=objectInput.readInt();
        if (this.getAge().get()!=0 && this.isAlive()) {
            long timeDiffInMilliSeconds=new Date().getTime() - this.getLastUpdate().getTime();
            long timeDiffInSeconds=TimeUnit.MILLISECONDS.toSeconds(timeDiffInMilliSeconds);
            long timesToUpdSatiety=timeDiffInSeconds/this.getNeedChangeTimeInSeconds();
            long satietyToSubstruct=timesToUpdSatiety*this.getSatietyChangePercent();
            long satietyToSubstructPlus=satietyToSubstruct*(-1);
            if (satietyToSubstructPlus>MAX_SATIETY)satietyToSubstructPlus=MAX_SATIETY;
            long satietyDiff=satiety-satietyToSubstructPlus;
            satiety=(int)satietyDiff;
        }
        if (satiety<=0){
            if (this.isAlive()) {
                this.setAlive(false);
                this.setLastUpdate(new Date());
            }
            satiety=0;
        }
        this.setSatiety(satiety);
    }

    @Override
    public String toString() {
        return this.getClass() + "   name:" + this.name + "    base:" + this.age.get() + "   satiety:" +
                this.satiety.get()+"    upd:"+this.getLastUpdate()+"    secBorn:"+this.getTimeToBeBornInSeconds()+
                "   isAlive: "+this.isAlive;
    }
}
