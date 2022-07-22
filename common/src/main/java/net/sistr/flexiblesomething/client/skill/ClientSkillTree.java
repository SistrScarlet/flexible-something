package net.sistr.flexiblesomething.client.skill;

public class ClientSkillTree {
    private final ClientSkill root;

    public ClientSkillTree(ClientSkill root) {
        this.root = root;
    }

    public ClientSkill getRoot() {
        return root;
    }

}
