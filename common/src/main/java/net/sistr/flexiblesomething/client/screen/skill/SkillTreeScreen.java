package net.sistr.flexiblesomething.client.screen.skill;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.sistr.flexiblesomething.client.skill.ClientSkillTree;

public class SkillTreeScreen extends Screen {
    private final AbstractSkillTreeUI skillTreeUI;
    private int clickAtX;
    private int clickAtY;
    private double deltaX;
    private double deltaY;
    private double scrollX;
    private double scrollY;
    private int scale = 30;

    public SkillTreeScreen(Text title, ClientSkillTree skillTree) {
        super(title);
        this.skillTreeUI = new SkillTreeUIReverse(this, skillTree,
                30, 20, 10, 10);
    }

    @Override
    public void tick() {
        super.tick();
        this.skillTreeUI.tick();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        this.skillTreeUI.setPos(scrollX + deltaX, scrollY + deltaY);
        this.skillTreeUI.setScale(this.scale / 30f);
        this.skillTreeUI.render(matrices, textRenderer);
    }

    //todo ドラッグ中にズームするとズレる
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.clickAtX = (int) mouseX;
        this.clickAtY = (int) mouseY;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        float scale = this.scale / 30f;
        this.deltaX = (mouseX - this.clickAtX) / scale;
        this.deltaY = (mouseY - this.clickAtY) / scale;
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean result = false;
        //あんまズレてないならクリック
        if (-5 <= this.deltaX && this.deltaX < 5
                && -5 <= this.deltaY && this.deltaY < 5) {
            result = this.skillTreeUI.mouseClicked(mouseX, mouseY, button);
        }
        this.scrollX += this.deltaX;
        this.scrollY += this.deltaY;
        this.deltaX = 0;
        this.deltaY = 0;
        return result || super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        scale += amount;
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
