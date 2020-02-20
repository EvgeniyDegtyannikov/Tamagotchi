package sample.entities;

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
import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class Pet implements Runnable, Externalizable {

    public static final int MIN_SATIETY = 1;
    public static final int MAX_SATIETY = 100;
    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 3;

    private String name;
    // ������ ��������
    private IntegerProperty age;
    // �������
    private IntegerProperty satiety;
    // ��������� ����� � ����� �������, ������������ ��� ����������� � ������
    private Date lastUpdate;
    // ��������� �� �� �����
    private boolean isAlive;

    // ��������� ���������� �������
    private int satietyChangeValue;
    // �����, ������� ������ ������ ����� ��������� �������
    private long timeToBeBornInSeconds;
    // ����� ����� ������������� ����������� ������������
    private long needChangeTimeInSeconds;

    // ��� ��� �������
    private Food food;
    private PetsImagesManager petsImagesManager;
    private PetsSerializationHelper petsSerializationHelper;

    {
        isAlive = true;
        this.age = new SimpleIntegerProperty(MIN_AGE);
        this.satiety = new SimpleIntegerProperty(MAX_SATIETY);
        this.lastUpdate = new Date();
        petsImagesManager = new PetsImagesManager();
        petsSerializationHelper = new PetsSerializationHelper();
    }

    // �����, ����������� ������������� �������
    public class PetsImagesManager {
        private Image age0Image;
        private Image age1Image;
        private Image age2Image;
        private Image age3Image;
        private Image ripImage;

        private PetsImagesManager() {
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

        public Image getRipImage() {
            return this.ripImage;
        }

        public void setRipImage(Image ripImage) {
            this.ripImage = ripImage;
        }

        // ���������� ����������� �������, ������ �� ��� �������� ��������. ���� ��� �������� ��������
        // ������� ����������� �����������, ���������� null.
        public Image getImageByAge() {
            switch (Pet.this.age.get()) {
                case 0: {
                    return this.getAge0Image();
                }
                case 1: {
                    return this.getAge1Image();
                }
                case 2: {
                    return this.getAge2Image();
                }
                case 3: {
                    return this.getAge3Image();
                }
                default: {
                    return null;
                }
            }
        }
    }

    @Override
    public void run() {
        Platform.runLater(this::naturalPetsStateChange);
    }

    public PetsImagesManager getPetsImagesManager() {
        return petsImagesManager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IntegerProperty getAge() {
        return age;
    }

    // ������������� �������, ���� ��� ��������� � �������� ����� MAX_AGE � MIN_AGE
    public void setAge(int age) {
        if (age <= MAX_AGE && age >= MIN_AGE) {
            if (this.age != null)
                this.age.set(age);
            else
                this.age = new SimpleIntegerProperty(age);
        }
    }

    public IntegerProperty getSatiety() {
        return satiety;
    }

    // ������������� ������� satiety, ���� ���������� �������� ��������� ������� ������, �� satiety
    // ��������������� � ������� �������, ���� ��� ������ ������������ ����������� �������, �� ��������������� �
    // MIN_SATIETY-1(�.�. � ���������������� ��������)
    private void setSatiety(int satiety) {
        if (satiety > MAX_SATIETY) satiety = MAX_SATIETY;
        if (satiety < MIN_SATIETY) satiety = MIN_SATIETY - 1;
        if (this.satiety == null)
            this.satiety = new SimpleIntegerProperty(satiety);
        else
            this.satiety.setValue(satiety);
    }

    public long getTimeToBeBornInSeconds() {
        return this.timeToBeBornInSeconds;
    }

    public void setTimeToBeBornInSeconds(long timeToBeBornInSeconds) {
        this.timeToBeBornInSeconds = timeToBeBornInSeconds;
    }

    public boolean isAlive() {
        return isAlive;
    }

    protected void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getSatietyChangeValue() {
        return satietyChangeValue;
    }

    public void setSatietyChangeValue(int satietyChangeValue) {
        this.satietyChangeValue = satietyChangeValue;
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
        this.lastUpdate = date;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    // �����, ������������ ������������ ��������� ��������� ������� � �������� �������. ������� satiety ��
    // satietyChangeValue, � � ������������ 15% ������������� ������� �������
    private void naturalPetsStateChange() {
        this.setSatiety(this.getSatiety().get() - this.getSatietyChangeValue());
        if (new Random().nextInt(100) < 15)
            this.setAge(this.getAge().get() + 1);
        System.out.println(this.toString());
    }

    // �����, ���������� � ������ � ������������� ��� ������ � ������������
    private class PetsSerializationHelper {
        // �����, ����������� ���������� �� �������� ����� ��� ���������� ����
        private long calculateRemainingTimeToBeBornOnWrite() {
            long timeDiffInMilliSeconds = new Date().getTime() - Pet.this.getLastUpdate().getTime();
            long timeDiffInSeconds = TimeUnit.MILLISECONDS.toSeconds(timeDiffInMilliSeconds);
            return Pet.this.getTimeToBeBornInSeconds() - timeDiffInSeconds;
        }

        // �����, ������������, ������ �� ���������� ������� ��� �������� ������� ��� �������� ����������
        private long validateRemainingTimeToBeBornOnRead(long time) {
            if (time <= 0) return 1;
            else return time;
        }

        // �����, �����������, �� ������� ����� ���������� ������� ������� ������� � ������� ���������� ����������.
        // ���� ������������ �������� ������� ������� ��������� ����������� ����������, �� ��� � ������� �
        // ���������������. ���� �������� �������� ������� ����� ��������� �������� ������� ����������� ������
        // ���������� �����������, �� ��� ��������������� �� 1�� ������ ���������� � ���������� ����� ������ �������.
        private int validateRemainingSatietyOnRead(int satiety) {
            if (Pet.this.getAge().get() != 0 && Pet.this.isAlive()) {
                long timeDiffInMilliSeconds = new Date().getTime() - Pet.this.getLastUpdate().getTime();
                long timeDiffInSeconds = TimeUnit.MILLISECONDS.toSeconds(timeDiffInMilliSeconds);
                long timesToUpdSatiety = timeDiffInSeconds / Pet.this.getNeedChangeTimeInSeconds();
                long satietyToSubstruct = timesToUpdSatiety * Pet.this.getSatietyChangeValue();
                if (satietyToSubstruct > MAX_SATIETY) satietyToSubstruct = MAX_SATIETY;
                long satietyDiff = satiety - satietyToSubstruct;
                satiety = (int) satietyDiff;
            }
            if (satiety < MIN_SATIETY) {
                Pet.this.die();
                satiety = MIN_SATIETY - 1;
            }
            return satiety;
        }
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeBoolean(this.isAlive());
        objectOutput.writeObject(this.getName());
        objectOutput.writeInt(this.getAge().intValue());
        objectOutput.writeLong(petsSerializationHelper.calculateRemainingTimeToBeBornOnWrite());
        if (this.isAlive()) this.setLastUpdate(new Date());
        objectOutput.writeObject(this.getLastUpdate());
        objectOutput.writeInt(this.getSatiety().intValue());
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.setAlive(objectInput.readBoolean());
        this.setName((String) objectInput.readObject());
        this.setAge((objectInput.readInt()));
        this.setTimeToBeBornInSeconds(petsSerializationHelper.validateRemainingTimeToBeBornOnRead(objectInput.readLong()));
        this.setLastUpdate((Date) objectInput.readObject());
        this.setSatiety(petsSerializationHelper.validateRemainingSatietyOnRead(objectInput.readInt()));
    }

    // �����, ���������� �� ������ �������. ���� ������� ��� �� ������ ������ ������, ������������� �������� isAlive �
    // false � ��������� ��������� �����.
    public void die() {
        if (this.isAlive()) {
            this.setAlive(false);
            this.setLastUpdate(new Date());
        }
    }

    // �����, ������������� �������� ������� �� satietyChangeValue
    public void feed() {
        this.setSatiety(this.getSatiety().get() + satietyChangeValue);
    }

    @Override
    public String toString() {
        return this.getClass() + "   name:" + this.name + "    age:" + this.age.get() + "   satiety:" +
                this.satiety.get() + "    upd:" + this.getLastUpdate() + "    secToBeBorn:" + this.getTimeToBeBornInSeconds() +
                "   isAlive: " + this.isAlive;
    }
}
