package net.sistr.flexiblesomething.skill;

public class SkillTree {
    private final Skill root;

    public SkillTree(Skill root) {
        this.root = root;
    }

    public Skill getRoot() {
        return root;
    }

    public static class Builder {
        private Skill.Builder root = new Skill.Builder("");

        public static Builder createBuilder() {
            return new Builder();
        }

        public static Skill.Builder createSkillBuilder(String name) {
            return new Skill.Builder(name);
        }

        public Builder setRoot(Skill.Builder root) {
            this.root = root;
            return this;
        }

        public SkillTree build() {
            return new SkillTree(root.build());
        }

    }
}
