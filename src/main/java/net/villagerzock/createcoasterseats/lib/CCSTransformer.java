package net.villagerzock.createcoasterseats.lib;

import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.EnumSet;
import java.util.List;

public class CCSTransformer implements ILaunchPluginService {

    private static final String CLIENT_ONLY =
            "Lnet/villagerzock/createcoasterseats/lib/ClientOnly;";

    @Override
    public String name() {
        return "ccs_transformer";
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
        return isEmpty
                ? EnumSet.noneOf(Phase.class)
                : EnumSet.of(Phase.BEFORE);
    }

    @Override
    public boolean processClass(Phase phase, ClassNode input, Type classType) {
        if (!isDedicatedServer()) {
            return false;
        }

        boolean methodsRemoved = input.methods.removeIf(this::hasClientOnlyAnnotation);
        boolean fieldsRemoved = input.fields.removeIf(this::hasClientOnlyAnnotation);

        return methodsRemoved || fieldsRemoved;
    }

    private boolean hasClientOnlyAnnotation(MethodNode method) {
        System.out.println("[CCS] Checking method: " + method.name + method.desc);

        boolean visible = hasAnnotation(method.visibleAnnotations);
        boolean invisible = hasAnnotation(method.invisibleAnnotations);

        System.out.println("[CCS] Method " + method.name + " -> visible=" + visible + ", invisible=" + invisible);

        return visible || invisible;
    }

    private boolean hasClientOnlyAnnotation(FieldNode field) {
        System.out.println("[CCS] Checking field: " + field.name + " (" + field.desc + ")");

        boolean visible = hasAnnotation(field.visibleAnnotations);
        boolean invisible = hasAnnotation(field.invisibleAnnotations);

        System.out.println("[CCS] Field " + field.name + " -> visible=" + visible + ", invisible=" + invisible);

        return visible || invisible;
    }

    private boolean hasAnnotation(List<AnnotationNode> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            System.out.println("[CCS] No annotations found");
            return false;
        }

        for (AnnotationNode annotation : annotations) {
            System.out.println("[CCS] Found annotation: " + annotation.desc);

            if (CLIENT_ONLY.equals(annotation.desc)) {
                System.out.println("[CCS] MATCH! ClientOnly annotation detected");
                return true;
            }
        }

        return false;
    }

    public CCSTransformer() {
        System.out.println("[CCS] TRANSFORMER CONSTRUCTED");
    }

    private boolean isDedicatedServer() {
        String launchTarget = Launcher.INSTANCE
                .environment()
                .getProperty(IEnvironment.Keys.LAUNCHTARGET.get())
                .orElseThrow();

        System.out.println("[CCS] Found launchtarget: " + launchTarget);
        return switch (launchTarget) {
            case "forgeserver", "forgeserverdev",
                 "neoforgeserver", "neoforgeserverdev" -> true;
            default -> false;
        };
    }
}